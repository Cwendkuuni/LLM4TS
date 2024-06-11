import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertEquals("name", var.getName());
        assertNull(var.getType());
    }

    @Test
    public void testUndeclare() {
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("name"));
    }

    @Test
    public void testIsDeclaredTrue() {
        scope.declare("name", null, null, null);
        assertTrue(scope.isDeclared("name", false));
    }

    @Test
    public void testIsDeclaredFalse() {
        assertFalse(scope.isDeclared("name", false));
    }

    @Test
    public void testIsDeclaredRecurseTrue() {
        Scope childScope = new Scope(scope, null);
        scope.declare("name", null, null, null);
        assertTrue(childScope.isDeclared("name", true));
    }

    @Test
    public void testIsDeclaredRecurseFalse() {
        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isDeclared("name", true));
    }

    @Test
    public void testGetVarExists() {
        Var var = scope.declare("name", null, null, null);
        assertEquals(var, scope.getVar("name"));
    }

    @Test
    public void testGetVarNotExists() {
        assertNull(scope.getVar("name"));
    }

    @Test
    public void testGetVars() {
        scope.declare("name1", null, null, null);
        scope.declare("name2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        assertFalse(scope.isLocal());
    }
}