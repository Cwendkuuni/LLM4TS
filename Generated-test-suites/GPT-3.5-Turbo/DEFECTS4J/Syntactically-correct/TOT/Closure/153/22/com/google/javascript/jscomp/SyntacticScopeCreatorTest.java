import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SyntacticScopeCreatorTest {
    
    private SyntacticScopeCreator scopeCreator;
    
    @BeforeEach
    public void setup() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }
    
    @Test
    public void testCreateScope_GlobalVariable_Declared() {
        Node rootNode = new Node(Token.SCRIPT);
        Scope parentScope = null;
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is the global scope
        assertTrue(scope.isGlobal());
        
        // Assert that the scope has no parent
        assertNull(scope.getParent());
        
        // Assert that the scope does not have any variables declared
        assertTrue(scope.getVars().isEmpty());
    }
    
    @Test
    public void testCreateScope_FunctionDeclaration_VariablesDeclaredInScope() {
        Node rootNode = createFunctionDeclarationNode();
        Scope parentScope = createParentScope();
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is not the global scope
        assertFalse(scope.isGlobal());
        
        // Assert that the parent scope is the expected parent scope
        assertEquals(parentScope, scope.getParent());
        
        // Assert that the scope has the expected number of variables declared
        assertEquals(3, scope.getVars().size());
        
        // Assert that the variables have been declared in the scope
        assertTrue(scope.isDeclared("x"));
        assertTrue(scope.isDeclared("y"));
        assertTrue(scope.isDeclared("z"));
    }
    
    @Test
    public void testCreateScope_FunctionDeclaration_WithFunctionExpression() {
        Node rootNode = createFunctionExpressionNode();
        Scope parentScope = createParentScope();
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is not the global scope
        assertFalse(scope.isGlobal());
        
        // Assert that the parent scope is the expected parent scope
        assertEquals(parentScope, scope.getParent());
        
        // Assert that the scope has the expected number of variables declared
        assertEquals(1, scope.getVars().size());
        
        // Assert that the variables have been declared in the scope
        assertTrue(scope.isDeclared("fnName"));
    }
    
    @Test
    public void testCreateScope_FunctionDeclaration_WithEmptyFunctionName() {
        Node rootNode = createEmptyFunctionNameNode();
        Scope parentScope = createParentScope();
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is not the global scope
        assertFalse(scope.isGlobal());
        
        // Assert that the parent scope is the expected parent scope
        assertEquals(parentScope, scope.getParent());
        
        // Assert that the scope has the expected number of variables declared
        assertEquals(0, scope.getVars().size());
    }
    
    @Test
    public void testCreateScope_CatchBlock_VariablesDeclaredInScope() {
        Node rootNode = createCatchBlockNode();
        Scope parentScope = createParentScope();
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is not the global scope
        assertFalse(scope.isGlobal());
        
        // Assert that the parent scope is the expected parent scope
        assertEquals(parentScope, scope.getParent());
        
        // Assert that the scope has the expected number of variables declared
        assertEquals(1, scope.getVars().size());
        
        // Assert that the variables have been declared in the scope
        assertTrue(scope.isDeclared("var"));
    }
    
    @Test
    public void testCreateScope_ScriptBlock_VariablesDeclaredInScope() {
        Node rootNode = createScriptBlockNode();
        Scope parentScope = null;
        
        Scope scope = scopeCreator.createScope(rootNode, parentScope);
        
        // Assert that the created scope is not null
        assertNotNull(scope);
        
        // Assert that the scope is the global scope
        assertTrue(scope.isGlobal());
        
        // Assert that the scope has no parent
        assertNull(scope.getParent());
        
        // Assert that the scope has the expected number of variables declared
        assertEquals(2, scope.getVars().size());
        
        // Assert that the variables have been declared in the scope
        assertTrue(scope.isDeclared("a"));
        assertTrue(scope.isDeclared("b"));
    }
    
    private Node createFunctionDeclarationNode() {
        Node rootNode = new Node(Token.FUNCTION);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node fnNameNode = new Node(Token.NAME, "fnName");
        Node argsNode = new Node(Token.LP);
        Node bodyNode = new Node(Token.BLOCK);
        
        rootNode.addChildToFront(fnNameNode);
        rootNode.addChildToBack(argsNode);
        rootNode.addChildToBack(bodyNode);
        
        Node arg1 = new Node(Token.NAME, "x");
        Node arg2 = new Node(Token.NAME, "y");
        Node arg3 = new Node(Token.NAME, "z");
        
        argsNode.addChildToFront(arg1);
        argsNode.addChildToBack(arg2);
        argsNode.addChildToBack(arg3);
        
        return rootNode;
    }
    
    private Node createFunctionExpressionNode() {
        Node rootNode = new Node(Token.FUNCTION);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node fnNameNode = new Node(Token.NAME, "");
        Node argsNode = new Node(Token.LP);
        Node bodyNode = new Node(Token.BLOCK);
        
        rootNode.addChildToFront(fnNameNode);
        rootNode.addChildToBack(argsNode);
        rootNode.addChildToBack(bodyNode);
        
        return rootNode;
    }
    
    private Node createEmptyFunctionNameNode() {
        Node rootNode = new Node(Token.FUNCTION);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node nameNode = new Node(Token.NAME, "");
        rootNode.addChildToFront(nameNode);
        
        return rootNode;
    }
    
    private Node createCatchBlockNode() {
        Node rootNode = new Node(Token.CATCH);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node varNode = new Node(Token.NAME, "var");
        Node blockNode = new Node(Token.BLOCK);
        
        rootNode.addChildToFront(varNode);
        rootNode.addChildToBack(blockNode);
        
        return rootNode;
    }
    
    private Node createScriptBlockNode() {
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node var1 = new Node(Token.NAME, "a");
        Node var2 = new Node(Token.NAME, "b");
        
        rootNode.addChildToFront(var1);
        rootNode.addChildToBack(var2);
        
        return rootNode;
    }
    
    private Scope createParentScope() {
        Node parentRoot = new Node(Token.SCRIPT);
        Scope parentScope = new Scope(parentRoot, new AbstractCompiler());
        
        parentScope.declare("parentVar", new Node(Token.NAME, "parentVar"), null, null);
        
        return parentScope;
    }
}