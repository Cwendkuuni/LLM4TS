import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertEquals("testVar", var.getName());
    }

    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVarInParentScope() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));

        Var var = parentScope.declare("parentVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertEquals(var, childScope.getVar("parentVar"));
    }

    @Test
    public void testGlobalScope() {
        Scope globalScope = new Scope(new Node(Token.BLOCK), null);
        assertTrue(globalScope.isGlobal());
        assertFalse(globalScope.isLocal());
    }

    @Test
    public void testLocalScope() {
        Scope parentScope = new Scope(null, null);
        Scope localScope = new Scope(parentScope, new Node(Token.FUNCTION));
        assertFalse(localScope.isGlobal());
        assertTrue(localScope.isLocal());
    }

}