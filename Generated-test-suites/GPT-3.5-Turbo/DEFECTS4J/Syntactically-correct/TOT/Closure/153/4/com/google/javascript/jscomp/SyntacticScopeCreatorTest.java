import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_FunctionWithNoArguments_DeclaresFunctionVariable() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        Node functionNameNode = new Node(Token.NAME, "testFunction");
        Node functionBodyNode = new Node(Token.BLOCK);
        functionNode.addChildToFront(functionNameNode);
        functionNode.addChildToBack(new Node(Token.EMPTY, ""));
        functionNode.addChildToBack(functionBodyNode);
        
        // Act
        Scope scope = scopeCreator.createScope(functionNode, null);
        String functionName = functionNameNode.getString();
        
        // Assert
        assertTrue(scope.isDeclared(functionName, false));
        assertEquals(functionNameNode, scope.getVar(functionName).getNode());
    }

    @Test
    public void testCreateScope_FunctionWithArguments_DeclaresFunctionVariables() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        Node functionNameNode = new Node(Token.NAME, "testFunction");
        Node functionArgsNode = new Node(Token.LP);
        Node functionArgNode1 = new Node(Token.NAME, "arg1");
        Node functionArgNode2 = new Node(Token.NAME, "arg2");
        functionArgsNode.addChildToFront(functionArgNode1);
        functionArgsNode.addChildToBack(functionArgNode2);
        Node functionBodyNode = new Node(Token.BLOCK);
        functionNode.addChildToFront(functionNameNode);
        functionNode.addChildToBack(functionArgsNode);
        functionNode.addChildToBack(functionBodyNode);
        
        // Act
        Scope scope = scopeCreator.createScope(functionNode, null);
        String functionName = functionNameNode.getString();
        
        // Assert
        assertTrue(scope.isDeclared(functionName, false));
        assertEquals(functionNameNode, scope.getVar(functionName).getNode());
        
        assertTrue(scope.isDeclared(functionArgNode1.getString(), false));
        assertEquals(functionArgNode1, scope.getVar(functionArgNode1.getString()).getNode());
        
        assertTrue(scope.isDeclared(functionArgNode2.getString(), false));
        assertEquals(functionArgNode2, scope.getVar(functionArgNode2.getString()).getNode());
    }

    @Test
    public void testCreateScope_CatchBlock_DeclaresCatchVariable() {
        // Arrange
        Node catchNode = new Node(Token.CATCH);
        Node catchVarNode = new Node(Token.NAME, "error");
        Node catchBlockNode = new Node(Token.BLOCK);
        catchNode.addChildToFront(catchVarNode);
        catchNode.addChildToBack(catchBlockNode);
        
        // Act
        Scope scope = scopeCreator.createScope(catchNode, null);
        String catchVar = catchVarNode.getString();
        
        // Assert
        assertTrue(scope.isDeclared(catchVar, false));
        assertEquals(catchVarNode, scope.getVar(catchVar).getNode());
    }

    @Test
    public void testCreateScope_GlobalBlock_DeclaresGlobalVariables() {
        // Arrange
        Node varNode = new Node(Token.VAR);
        Node varNode1 = new Node(Token.NAME, "x");
        Node varNode2 = new Node(Token.NAME, "y");
        Node varNode3 = new Node(Token.NAME, "z");
        varNode.addChildToBack(varNode1);
        varNode.addChildToBack(varNode2);
        varNode.addChildToBack(varNode3);
        Node globalBlock = new Node(Token.SCRIPT);
        globalBlock.addChildToBack(varNode);
        
        // Act
        Scope scope = scopeCreator.createScope(globalBlock, null);
        
        // Assert
        assertTrue(scope.isDeclared(varNode1.getString(), false));
        assertEquals(varNode1, scope.getVar(varNode1.getString()).getNode());
        
        assertTrue(scope.isDeclared(varNode2.getString(), false));
        assertEquals(varNode2, scope.getVar(varNode2.getString()).getNode());
        
        assertTrue(scope.isDeclared(varNode3.getString(), false));
        assertEquals(varNode3, scope.getVar(varNode3.getString()).getNode());
    }

    @Test
    public void testCreateScope_ControlStructure_DeclaresVariablesInControlStructure() {
        // Arrange
        Node ifNode = new Node(Token.IF);
        Node varNode = new Node(Token.VAR);
        Node varNode1 = new Node(Token.NAME, "x");
        varNode.addChildToBack(varNode1);
        ifNode.addChildToFront(varNode);
        
        // Act
        Scope scope = scopeCreator.createScope(ifNode, null);
        
        // Assert
        assertTrue(scope.isDeclared(varNode1.getString(), false));
        assertEquals(varNode1, scope.getVar(varNode1.getString()).getNode());
    }

    @Test
    public void testDeclareVar_VariableAlreadyDeclared_CallsRedeclarationHandler() {
        // Arrange
        MockRedeclarationHandler redeclarationHandler = new MockRedeclarationHandler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
        Node varNode1 = new Node(Token.NAME, "x");
        Node varNode2 = new Node(Token.NAME, "y");
        Node varNode3 = new Node(Token.NAME, "z");
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(varNode1);
        parent.addChildToBack(varNode2);
        parent.addChildToBack(varNode3);
        
        // Act
        Scope scope = scopeCreator.createScope(parent, null);
        
        // Assert
        assertTrue(redeclarationHandler.onRedeclarationCalled);
    }

    @Test
    public void testDeclareVar_VariableNotDeclared_DeclaresVariable() {
        // Arrange
        MockRedeclarationHandler redeclarationHandler = new MockRedeclarationHandler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
        Node varNode = new Node(Token.NAME, "x");
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(varNode);
        
        // Act
        Scope scope = scopeCreator.createScope(parent, null);
        
        // Assert
        assertTrue(scope.isDeclared(varNode.getString(), false));
        assertEquals(varNode, scope.getVar(varNode.getString()).getNode());
    }
    
    // Mock Redeclearation Handler to be used during testing
    private class MockRedeclarationHandler implements SyntacticScopeCreator.RedeclarationHandler {
        public boolean onRedeclarationCalled = false;
        
        @Override
        public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
            onRedeclarationCalled = true;
        }
    }
}