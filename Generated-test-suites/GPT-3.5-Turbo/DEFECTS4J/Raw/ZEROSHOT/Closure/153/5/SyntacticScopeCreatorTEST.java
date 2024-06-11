import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @After
    public void tearDown() {
        scopeCreator = null;
    }

    @Test
    public void testCreateScope_withFunctionNode_returnsScope() {
        Node node = new Node(Token.FUNCTION);
        Scope parentScope = new Scope(node, new AbstractCompiler());
        Scope scope = scopeCreator.createScope(node, parentScope);
        assertNotNull(scope);
    }

    @Test
    public void testCreateScope_withNonFunctionNode_returnsScope() {
        Node node = new Node(Token.SCRIPT);
        Scope scope = scopeCreator.createScope(node, null);
        assertNotNull(scope);
    }

    @Test
    public void testDeclareVar_withNewVariable_declaresVariable() {
        String name = "testVar";
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node();

        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        Scope.Var var = scopeCreator.scope.getVar(name);
        assertNotNull(var);
        assertEquals(name, var.name);
    }

    @Test
    public void testDeclareVar_withRedeclaredVariable_reportsError() {
        String name = "testVar";
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node origParent = new Node(Token.SCRIPT);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node();

        Scope.Var origVar = new Scope.Var(name, origParent, null, compiler.getInput(sourceName));
        scopeCreator.scope.declare(name, origVar);

        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // Verify that an error was reported
        JSError[] reportedErrors = compiler.getErrors();
        boolean errorFound = false;
        for (JSError error : reportedErrors) {
            if (error.getType().equals(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR)) {
                errorFound = true;
                break;
            }
        }
        assertTrue(errorFound);
    }

    @Test
    public void testDeclareVar_withShadowingArgumentsVariable_reportsError() {
        String name = "arguments";
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node();

        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // Verify that an error was reported
        JSError[] reportedErrors = compiler.getErrors();
        boolean errorFound = false;
        for (JSError error : reportedErrors) {
            if (error.getType().equals(SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR)) {
                errorFound = true;
                break;
            }
        }
        assertTrue(errorFound);
    }

    @Test
    public void testScanRoot_withFunctionNode_scansVariables() {
        Node node = new Node(Token.FUNCTION);
        node.putProp(Node.SOURCENAME_PROP, "testSource");
        Node fnNameNode = new Node(Token.NAME, "testFunction");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        node.addChildToBack(fnNameNode);
        node.addChildToBack(args);
        node.addChildToBack(body);

        scopeCreator.scanRoot(node, null);

        // Verify that the function name was declared
        Scope.Var fnNameVar = scopeCreator.scope.getVar("testFunction");
        assertNotNull(fnNameVar);

        // Verify that the arguments variable was declared
        Scope.Var argumentsVar = scopeCreator.scope.getVar("arguments");
        assertNotNull(argumentsVar);

        // Verify that variables in the body were declared
        Scope.Var bodyVar = scopeCreator.scope.getVar("testVar");
        assertNotNull(bodyVar);
    }

    @Test
    public void testScanRoot_withGlobalBlock_scansVariables() {
        Node node = new Node(Token.SCRIPT);
        node.putProp(Node.SOURCENAME_PROP, "testSource");
        Node varNameNode = new Node(Token.NAME, "testVar");
        Node varNode = new Node(Token.VAR, varNameNode);
        node.addChildToBack(varNode);

        scopeCreator.scanRoot(node, null);

        // Verify that the variable was declared
        Scope.Var var = scopeCreator.scope.getVar("testVar");
        assertNotNull(var);
    }

    @Test
    public void testScanVars_withVarNode_scansVariables() {
        Node node = new Node(Token.VAR);
        Node varNameNode1 = new Node(Token.NAME, "testVar1");
        Node varNameNode2 = new Node(Token.NAME, "testVar2");
        node.addChildToBack(varNameNode1);
        node.addChildToBack(varNameNode2);

        scopeCreator.scanVars(node, null);

        // Verify that the variables were declared
        Scope.Var var1 = scopeCreator.scope.getVar("testVar1");
        assertNotNull(var1);
        Scope.Var var2 = scopeCreator.scope.getVar("testVar2");
        assertNotNull(var2);
    }

    @Test
    public void testScanVars_withFunctionNodeAndFunctionExpression_doesNotDeclareVariable() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(NodeUtil.FUNCTION_EXPRESSION, true);
        Node varNameNode = new Node(Token.NAME, "testVar");
        node.addChildToBack(varNameNode);

        scopeCreator.scanVars(node, null);

        // Verify that the variable was not declared
        Scope.Var var = scopeCreator.scope.getVar("testVar");
        assertNull(var);
    }

    @Test
    public void testScanVars_withCatchesNode_scansVariables() {
        Node node = new Node(Token.CATCH);
        Node varNameNode = new Node(Token.NAME, "testVar");
        Node block = new Node(Token.BLOCK);
        node.addChildToBack(varNameNode);
        node.addChildToBack(block);

        scopeCreator.scanVars(node, null);

        // Verify that the variable was declared
        Scope.Var var = scopeCreator.scope.getVar("testVar");
        assertNotNull(var);
    }

    @Test
    public void testScanVars_withControlStructureNode_scansVariables() {
        Node node = new Node(Token.IF);
        Node condition = new Node(Token.TRUE);
        Node ifBlock = new Node(Token.BLOCK);
        node.addChildToBack(condition);
        node.addChildToBack(ifBlock);

        scopeCreator.scanVars(node, null);
    }

    @Test
    public void testScanVars_withStatementBlockNode_scansVariables() {
        Node node = new Node(Token.BLOCK);
        Node varNameNode = new Node(Token.NAME, "testVar");
        Node varNode = new Node(Token.VAR, varNameNode);
        node.addChildToBack(varNode);

        scopeCreator.scanVars(node, null);

        // Verify that the variable was declared
        Scope.Var var = scopeCreator.scope.getVar("testVar");
        assertNotNull(var);
    }
}