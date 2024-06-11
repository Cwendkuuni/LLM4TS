import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void createScope_withParent_scopeCreatedWithParent() {
        Node n = mock(Node.class);
        Scope parentScope = mock(Scope.class);
        Node sourceNode = mock(Node.class);
        when(n.getProp(Node.SOURCENAME_PROP)).thenReturn("testSource");

        Scope scope = scopeCreator.createScope(n, parentScope);

        assertSame(parentScope, scope.getParent());
    }

    @Test
    public void createScope_withoutParent_scopeCreatedWithoutParent() {
        Node n = mock(Node.class);
        Node sourceNode = mock(Node.class);
        when(n.getProp(Node.SOURCENAME_PROP)).thenReturn("testSource");

        Scope scope = scopeCreator.createScope(n, null);

        assertSame(null, scope.getParent());
    }

    @Test
    public void scanVars_withVarDeclaration_varDeclaredInScope() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node child1 = mock(Node.class);
        Node varNode = mock(Node.class);

        when(varNode.getType()).thenReturn(Token.VAR);
        when(child1.getType()).thenReturn(Token.NAME);
        when(child1.getString()).thenReturn("x");
        when(parent.getFirstChild()).thenReturn(child1);
        when(n.getFirstChild()).thenReturn(varNode);
        when(n.getChildCount()).thenReturn(1);

        scopeCreator.scanVars(n, parent);

        assertSame("x", scopeCreator.scope.getVar("x").name);
    }

    @Test
    public void scanVars_withFunctionDeclaration_functionNameDeclaredInScope() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node child1 = mock(Node.class);
        Node fnNode = mock(Node.class);

        when(fnNode.getType()).thenReturn(Token.FUNCTION);
        when(fnNode.getFirstChild()).thenReturn(child1);
        when(child1.getString()).thenReturn("fnName");
        when(n.getFirstChild()).thenReturn(fnNode);
        when(NodeUtil.isFunctionExpression(n)).thenReturn(false);

        scopeCreator.scanVars(n, parent);

        assertSame("fnName", scopeCreator.scope.getVar("fnName").name);
    }

    @Test
    public void scanVars_withCatchDeclaration_catchVarDeclaredInScope() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node child1 = mock(Node.class);
        Node catchNode = mock(Node.class);
        Node blockNode = mock(Node.class);

        when(catchNode.getType()).thenReturn(Token.CATCH);
        when(catchNode.getFirstChild()).thenReturn(child1);
        when(child1.getType()).thenReturn(Token.NAME);
        when(child1.getString()).thenReturn("catchVar");
        when(n.getFirstChild()).thenReturn(catchNode);
        when(n.getChildCount()).thenReturn(2);
        when(child1.getNext()).thenReturn(blockNode);

        scopeCreator.scanVars(n, parent);

        assertSame("catchVar", scopeCreator.scope.getVar("catchVar").name);
    }

    @Test
    public void scanVars_withControlStructure_varDeclaredInScope() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        Node child3 = mock(Node.class);
        Node varNode = mock(Node.class);

        when(varNode.getType()).thenReturn(Token.VAR);
        when(child1.getType()).thenReturn(Token.NAME);
        when(child1.getString()).thenReturn("x");
        when(child2.getType()).thenReturn(Token.NAME);
        when(child2.getString()).thenReturn("y");
        when(child3.getType()).thenReturn(Token.NAME);
        when(child3.getString()).thenReturn("z");
        when(n.getFirstChild()).thenReturn(varNode);
        when(varNode.getFirstChild()).thenReturn(child1);
        when(child1.getNext()).thenReturn(child2);
        when(child2.getNext()).thenReturn(child3);
        when(n.getNext()).thenReturn(null);
        when(NodeUtil.isControlStructure(n)).thenReturn(true);

        scopeCreator.scanVars(n, parent);

        assertSame("x", scopeCreator.scope.getVar("x").name);
        assertSame("y", scopeCreator.scope.getVar("y").name);
        assertSame("z", scopeCreator.scope.getVar("z").name);
    }

    @Test
    public void declareVar_withDuplicateDeclaration_duplicatedErrorReported() {
        String name = "x";
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node gramps = mock(Node.class);
        Node nodeWithLineNumber = mock(Node.class);
        Scope.Var origVar = mock(Scope.Var.class);
        Node origParent = mock(Node.class);
        Node errorNode = mock(Node.class);
        JSDocInfo info = mock(JSDocInfo.class);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getJSDocInfo()).thenReturn(info);
        when(info.getSuppressions()).thenReturn(new HashSet<>(Arrays.asList("duplicate")));
        when(scopeCreator.scope.isGlobal()).thenReturn(true);
        when(scopeCreator.scope.getVar(name)).thenReturn(origVar);
        when(origVar.getParentNode()).thenReturn(origParent);
        when(origParent.getType()).thenReturn(Token.CATCH);

        scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);

        Mockito.verify(compiler).report(JSError.make(scopeCreator.sourceName, n, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, name, (origVar.input != null
                ? origVar.input.getName()
                : "??")));
    }

    @Test
    public void declareVar_withDuplicateDeclarationInCatch_catchVarsDeclaredMultipleTimes() {
        String name = "x";
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node gramps = mock(Node.class);
        Node nodeWithLineNumber = mock(Node.class);
        Scope.Var origVar = mock(Scope.Var.class);
        Node origParent = mock(Node.class);
        Node errorNode = mock(Node.class);
        JSDocInfo info = mock(JSDocInfo.class);
 
        when(info.getSuppressions()).thenReturn(new HashSet<>());
        when(scopeCreator.scope.isGlobal()).thenReturn(true);
        when(scopeCreator.scope.getVar(name)).thenReturn(origVar);
        when(origVar.getParentNode()).thenReturn(origParent);
        when(origParent.getType()).thenReturn(Token.CATCH);
        when(parent.getType()).thenReturn(Token.CATCH);

        scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);

        Mockito.verify(compiler, Mockito.times(0)).report(JSError.make(scopeCreator.sourceName, n, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, name, (origVar.input != null
                ? origVar.input.getName()
                : "??")));
    }

    @Test
    public void declareVar_withShadowingArgumentsArgumentsVariableDeemedAsDuplicatedErrorReported() {
        String name = "arguments";
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node gramps = mock(Node.class);
        Node nodeWithLineNumber = mock(Node.class);

        when(scopeCreator.sourceName).thenReturn("testSource");
        when(scopeCreator.scope.isGlobal()).thenReturn(false);

        scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);

        Mockito.verify(compiler).report(JSError.make(scopeCreator.sourceName, n, SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR));
    }
}