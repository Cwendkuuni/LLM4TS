import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
    
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, new Node(Token.BLOCK));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.UNKNOWN, var.getType());
    }
    
    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, new Node(Token.BLOCK));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(scope.getVar(var.getName()));
        scope.undeclare(var);
        assertNull(scope.getVar(var.getName()));
    }
    
    @Test
    public void testGetVarInParentScope() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK));
        Scope scope = new Scope(parentScope, new Node(Token.FUNCTION));
        Var var = parentScope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(scope.getVar(var.getName()));
    }
    
    @Test
    public void testGetSlot() {
        Scope scope = new Scope(null, new Node(Token.BLOCK));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        StaticSlot<JSType> slot = scope.getSlot(var.getName());
        assertNotNull(slot);
        assertEquals(var, slot);
    }
    
    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, new Node(Token.BLOCK));
        Var var1 = scope.declare("testVar1", new Node(Token.NAME), JSType.UNKNOWN, null);
        Var var2 = scope.declare("testVar2", new Node(Token.NAME), JSType.STRING, null);
        Iterator<Var> iter = scope.getVars();
        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, new Node(Token.BLOCK));
        scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", true));
        assertFalse(scope.isDeclared("unknownVar", true));
    }
    
    @Test
    public void testIsGlobal() {
        Scope globalScope = new Scope(null, new Node(Token.BLOCK));
        assertTrue(globalScope.isGlobal());
        
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
        assertFalse(localScope.isGlobal());
    }
    
    @Test
    public void testIsLocal() {
        Scope globalScope = new Scope(null, new Node(Token.BLOCK));
        assertFalse(globalScope.isLocal());
        
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
        assertTrue(localScope.isLocal());
    }
}