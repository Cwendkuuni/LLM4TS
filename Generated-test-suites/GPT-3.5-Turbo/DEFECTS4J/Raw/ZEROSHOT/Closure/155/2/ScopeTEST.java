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
    public void testGetDepth() {
        assertEquals(0, scope.getDepth());
    }

    @Test
    public void testIsBottom() {
        assertFalse(scope.isBottom());
    }

    @Test
    public void testGetRootNode() {
        assertNull(scope.getRootNode());
    }

    @Test
    public void testGetParent() {
        assertNull(scope.getParent());
    }

    @Test
    public void testGetGlobalScope() {
        assertEquals(scope, scope.getGlobalScope());
    }

    @Test
    public void testGetParentScope() {
        assertNull(scope.getParentScope());
    }

    @Test
    public void testGetTypeOfThis() {
        assertNull(scope.getTypeOfThis());
    }

    @Test
    public void testDeclare() {
        Scope.Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
    }

    @Test
    public void testUndeclare() {
        Scope.Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetSlot() {
        Scope.Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        Scope.Var slot = (Scope.Var) scope.getSlot("testVar");
        assertNotNull(slot);
        assertEquals(var, slot);
    }

    @Test
    public void testGetOwnSlot() {
        Scope.Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        Scope.Var slot = (Scope.Var) scope.getOwnSlot("testVar");
        assertNotNull(slot);
        assertEquals(var, slot);
    }

    @Test
    public void testIsDeclared() {
        Scope.Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertTrue(scope.isDeclared("testVar", true));
    }

    @Test
    public void testGetVars() {
        Scope.Var var1 = scope.declare("testVar1", null, null, null);
        Scope.Var var2 = scope.declare("testVar2", null, null, null);
        assertNotNull(var1);
        assertNotNull(var2);
        assertEquals(2, scope.getVarCount());
        assertTrue(scope.getVars().hasNext());
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