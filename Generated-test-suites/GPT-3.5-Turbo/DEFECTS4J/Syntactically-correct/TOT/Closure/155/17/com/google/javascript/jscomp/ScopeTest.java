import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, new Node(Token.SCRIPT));

        // Declare a variable
        Var var = scope.declare("x", null, null, null);

        // Test if the variable is declared
        assertNotNull(scope.getVar("x"));
        
        // Test if the variable is in the scope
        assertTrue(scope.isDeclared("x", true));
        
        // Test if the variable is not in the parent scope
        assertFalse(scope.isDeclared("x", false));
    }
    
    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, new Node(Token.SCRIPT));

        // Declare a variable
        Var var = scope.declare("x", null, null, null);
        
        // Undeclare the variable
        scope.undeclare(var);

        // Test if the variable is undeclared
        assertNull(scope.getVar("x"));
    }
    
    @Test
    public void testGlobalScope() {
        Scope scope = new Scope(null, new Node(Token.SCRIPT));

        // Test if the scope is global
        assertTrue(scope.isGlobal());
        
        // Test if the scope is not local
        assertFalse(scope.isLocal());
    }
    
    @Test
    public void testLocalScope() {
        Scope parentScope = new Scope(null, new Node(Token.SCRIPT));
        Scope scope = new Scope(parentScope, new Node(Token.FUNCTION));

        // Test if the scope is not global
        assertFalse(scope.isGlobal());
        
        // Test if the scope is local
        assertTrue(scope.isLocal());
    }

}