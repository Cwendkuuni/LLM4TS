import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.UNKNOWN, var.getType());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVar() {
        Scope parentScope = new Scope(null, new Node(Token.FUNCTION));
        Scope scope = new Scope(parentScope, new Node(Token.FUNCTION));
        Var var1 = parentScope.declare("testVar1", new Node(Token.NAME), JSType.UNKNOWN, null);
        Var var2 = scope.declare("testVar2", new Node(Token.NAME), JSType.NUMBER, null);
        assertEquals(var1, scope.getVar("testVar1"));
        assertEquals(var2, scope.getVar("testVar2"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", false));
        assertFalse(scope.isDeclared("unknownVar", false));
    }

    @Test
    public void testIsGlobal() {
        Scope globalScope = new Scope(new Node(Token.SCRIPT), null);
        assertTrue(globalScope.isGlobal());
        assertFalse(globalScope.isLocal());
    }

    @Test
    public void testIsLocal() {
        Scope localScope = new Scope(null, new Node(Token.FUNCTION));
        assertFalse(localScope.isGlobal());
        assertTrue(localScope.isLocal());
    }
}