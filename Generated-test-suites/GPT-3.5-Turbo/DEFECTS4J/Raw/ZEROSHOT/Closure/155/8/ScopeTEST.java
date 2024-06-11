import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var1", null, null, null);
        assertNotNull(var);
        assertEquals("var1", var.getName());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var1", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("var1"));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var1", null, null, null);
        assertEquals(var, scope.getVar("var1"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        assertTrue(scope.isDeclared("var1", false));
        assertFalse(scope.isDeclared("var2", false));
    }

    @Test
    public void testIsGlobal() {
        Scope globalScope = new Scope(null, null);
        assertTrue(globalScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope localScope = new Scope(new Scope(null, null), null);
        assertTrue(localScope.isLocal());
    }
}