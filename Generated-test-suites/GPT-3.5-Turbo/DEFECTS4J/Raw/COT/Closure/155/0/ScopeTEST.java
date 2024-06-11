
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertEquals("name", var.getName());
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isDeclared("name", false));
        scope.declare("name", null, null, null);
        assertTrue(scope.isDeclared("name", false));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        assertNull(scope.getVar("name"));
        Var var = scope.declare("name", null, null, null);
        assertEquals(var, scope.getVar("name"));

        Scope parentScope = new Scope(null, null);
        parentScope.declare("name", null, null, null);
        scope = new Scope(parentScope, null);
        assertEquals(var, scope.getVar("name"));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.getVars().hasNext());
        scope.declare("name1", null, null, null);
        scope.declare("name2", null, null, null);
        assertTrue(scope.getVars().hasNext());
        assertEquals("name1", scope.getVars().next().getName());
        assertEquals("name2", scope.getVars().next().getName());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        assertEquals(0, scope.getVarCount());
        scope.declare("name1", null, null, null);
        scope.declare("name2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());
        Scope parentScope = new Scope(null, null);
        scope = new Scope(parentScope, null);
        assertFalse(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isLocal());
        Scope parentScope = new Scope(null, null);
        scope = new Scope(parentScope, null);
        assertTrue(scope.isLocal());
    }
}
