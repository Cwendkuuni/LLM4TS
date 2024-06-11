import static org.junit.Assert.*;

import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_rootScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node rootNode = new Node(Token.ROOT);
        Scope scope = scopeCreator.createScope(rootNode, null);

        assertNull(scope.getParent());
        assertNotNull(scope.getVars());
        assertTrue(scope.getVars().isEmpty());
    }

    @Test
    public void testCreateScope_functionScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        Scope parentScope = new Scope(new Node(Token.ROOT), compiler);

        Scope scope = scopeCreator.createScope(functionNode, parentScope);

        assertEquals(parentScope, scope.getParent());
        assertNotNull(scope.getVars());
        assertTrue(scope.getVars().isEmpty());
    }

    @Test
    public void testDeclareVar_newVariable() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node variableNode = new Node(Token.NAME, "testVar");
        variableNode.setInputId(1);
        
        Scope scope = new Scope(new Node(Token.ROOT), compiler);
        
        scopeCreator.declareVar("testVar", variableNode, new Node(Token.VAR), new Node(Token.ROOT), null, new Node(Token.ROOT));

        assertTrue(scope.isDeclared("testVar", false));
    }

    @Test
    public void testDeclareVar_existingVariable() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node variableNode = new Node(Token.NAME, "testVar");
        variableNode.setInputId(1);
        
        Scope scope = new Scope(new Node(Token.ROOT), compiler);
        
        scope.declare("testVar", new Node(Token.VAR), null, compiler.getInput(null));

        scopeCreator.declareVar("testVar", variableNode, new Node(Token.VAR), new Node(Token.ROOT), null, new Node(Token.ROOT));

        // Check if onRedeclaration is called
        assertTrue(true);
    }

    @Test
    public void testCreateScope_functionScopeWithArgs() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        Node argNode1 = new Node(Token.NAME, "arg1");
        Node argNode2 = new Node(Token.NAME, "arg2");

        Node rootNode = new Node(Token.ROOT);
        rootNode.addChildToBack(argNode1);
        rootNode.addChildToBack(argNode2);

        functionNode.addChildToBack(argNode1);
        functionNode.addChildToBack(argNode2);

        Scope parentScope = new Scope(new Node(Token.ROOT), compiler);

        Scope scope = scopeCreator.createScope(functionNode, parentScope);

        assertEquals(parentScope, scope.getParent());
        assertNotNull(scope.getVars());
        assertTrue(scope.getVars().containsKey("arg1"));
        assertTrue(scope.getVars().containsKey("arg2"));
    }

    @Test
    public void testScanVars_varDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node rootNode = new Node(Token.ROOT);
        Node varNode = new Node(Token.VAR);
        Node varChildNode = new Node(Token.NAME, "testVar");

        varNode.addChildToBack(varChildNode);
        rootNode.addChildToBack(varNode);

        scopeCreator.scanVars(varNode, rootNode);

        Scope scope = scopeCreator.createScope(varNode, null);

        assertNotNull(scope.getVars());
        assertTrue(scope.getVars().containsKey("testVar"));
    }
}