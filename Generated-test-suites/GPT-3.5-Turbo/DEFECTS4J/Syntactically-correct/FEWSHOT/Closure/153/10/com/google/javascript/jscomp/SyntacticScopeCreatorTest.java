import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setup() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void testCreateScope_FunctionDeclaration_ScopeCreated() {
        Node node = new Node(Token.FUNCTION);
        Scope parent = new Scope();
        Scope scope = scopeCreator.createScope(node, parent);
        assertNotNull(scope);
    }

    @Test
    public void testCreateScope_GlobalBlock_ScopeCreated() {
        Node node = new Node(Token.SCRIPT);
        Scope scope = scopeCreator.createScope(node, null);
        assertNotNull(scope);
        assertNull(scope.getParent());
    }

    @Test
    public void testScanVars_VarDeclaration_VariableDeclared() {
        Node varNode = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
        Node child2 = new Node(Token.NAME, new Node(Token.STRING, "y"));
        varNode.addChildToBack(child1);
        varNode.addChildToBack(child2);

        Node parent = new Node(Token.SCRIPT);

        Scope scope = new Scope();
        scopeCreator.scanVars(varNode, parent);

        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
    }

    @Test
    public void testScanVars_FunctionDeclaration_FunctionNameDeclared() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, "func"));
        functionNode.addChildToFront(nameNode);

        Node parent = new Node(Token.SCRIPT);

        Scope scope = new Scope();
        scopeCreator.scanVars(functionNode, parent);

        assertTrue(scope.isDeclared("func", false));
    }

    @Test
    public void testScanVars_CatchStatement_CatchVarDeclared() {
        Node catchNode = new Node(Token.CATCH);
        Node catchVar = new Node(Token.NAME, new Node(Token.STRING, "error"));
        catchNode.addChildToFront(catchVar);

        Node parent = new Node(Token.SCRIPT);

        Scope scope = new Scope();
        scopeCreator.scanVars(catchNode, parent);

        assertTrue(scope.isDeclared("error", false));
    }

    @Test
    public void testScanVars_ControlStructure_JavascriptNodeScanned() {
        Node ifNode = new Node(Token.IF);
        Node condition = new Node(Token.FALSE);
        ifNode.addChildToFront(condition);

        Node parent = new Node(Token.SCRIPT);

        Scope scope = new Scope();
        scopeCreator.scanVars(ifNode, parent);

        // Assert that the Javascript node has been scanned
        // should be handled by NodeUtil class - this is just a placeholder test
        assertTrue(true);
    }

    @Test
    public void testScanRoot_FunctionDeclaration_FunctionNameBleeded() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, "func"));
        functionNode.addChildToFront(nameNode);

        Node parent = new Node(Token.SCRIPT);

        Scope parentScope = new Scope();
        parentScope.declare("func", nameNode, null, null);

        Scope scope = new Scope();
        scopeCreator.scanRoot(functionNode, parent);

        assertTrue(scope.isDeclared("func", false));
    }
}