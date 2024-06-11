import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {
    
    private SyntacticScopeCreator scopeCreator;
    
    @Before
    public void setup() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }
    
    @Test
    public void createScope_GlobalBlock_ScopeCreated() {
        Node n = createNode(Token.SCRIPT);
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
    }
    
    @Test
    public void createScope_FunctionDeclaration_ScopeCreatedWithFunctionName() {
        Node n = createNode(Token.FUNCTION);
        n.addChildToBack(createNode(Token.NAME, "test"));
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertNotNull(scope.getVar("test"));
        assertEquals("test", scope.getVar("test").getName());
    }
    
    @Test
    public void createScope_FunctionExpression_ScopeCreatedWithoutFunctionName() {
        Node n = createNode(Token.FUNCTION);
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertNull(scope.getVar("test"));
    }
    
    @Test
    public void createScope_CatchBlock_ScopeCreatedWithCatchVar() {
        Node n = createNode(Token.CATCH);
        n.addChildToBack(createNode(Token.NAME, "error"));
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertNotNull(scope.getVar("error"));
        assertEquals("error", scope.getVar("error").getName());
    }
    
    @Test
    public void createScope_FunctionArguments_ArgumentsVariableDeclared() {
        Node n = createNode(Token.FUNCTION);
        n.addChildToBack(createNode(Token.NAME, "test"));
        n.addChildToBack(createNode(Token.LP));
        n.getLastChild()
         .addChildToBack(createNode(Token.NAME, "arg1"));
        n.getLastChild()
         .addChildToBack(createNode(Token.NAME, "arg2"));
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertNotNull(scope.getVar("arguments"));
        assertEquals("arguments", scope.getVar("arguments").getName());
        assertNotNull(scope.getVar("arg1"));
        assertEquals("arg1", scope.getVar("arg1").getName());
        assertNotNull(scope.getVar("arg2"));
        assertEquals("arg2", scope.getVar("arg2").getName());
    }
    
    @Test
    public void createScope_VariableDeclaration_VariablesDeclared() {
        Node n = createNode(Token.VAR);
        n.addChildToBack(createNode(Token.NAME, "x"));
        n.addChildToBack(createNode(Token.NAME, "y"));
        n.addChildToBack(createNode(Token.NAME, "z"));
        Scope parent = null;
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertNotNull(scope.getVar("x"));
        assertEquals("x", scope.getVar("x").getName());
        assertNotNull(scope.getVar("y"));
        assertEquals("y", scope.getVar("y").getName());
        assertNotNull(scope.getVar("z"));
        assertEquals("z", scope.getVar("z").getName());
    }
    
    private Node createNode(int type) {
        return new Node(type);
    }
    
    private Node createNode(int type, String name) {
        return Node.newString(Token.NAME, name);
    }
}