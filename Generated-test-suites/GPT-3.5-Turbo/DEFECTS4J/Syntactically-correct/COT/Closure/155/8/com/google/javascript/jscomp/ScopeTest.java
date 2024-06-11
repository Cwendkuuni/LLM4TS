
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("myVar", null, null, null);
        assertNotNull(var);
        assertEquals("myVar", var.getName());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("myVar", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("myVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("myVar", null, null, null);
        assertTrue(scope.isDeclared("myVar", true));
        assertFalse(scope.isDeclared("otherVar", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("myVar1", null, null, null);
        scope.declare("myVar2", null, null, null);
        assertEquals(2, scope.getVarCount());
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        assertEquals("myVar1", iterator.next().getName());
        assertTrue(iterator.hasNext());
        assertEquals("myVar2", iterator.next().getName());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(new Scope(null, null), null);
        assertTrue(scope.isLocal());
    }
}

