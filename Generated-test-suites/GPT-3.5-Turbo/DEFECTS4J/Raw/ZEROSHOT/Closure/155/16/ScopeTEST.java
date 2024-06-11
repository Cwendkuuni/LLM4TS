import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

    private Scope testScope;

    @Before
    public void setUp() {
        testScope = new Scope(null, null);
    }

    @Test
    public void testGetDepth() {
        assertEquals(0, testScope.getDepth());
    }

    @Test
    public void testIsBottom() {
        assertFalse(testScope.isBottom());
    }

    @Test
    public void testGetRootNode() {
        assertNull(testScope.getRootNode());
    }

    @Test
    public void testGetParent() {
        assertNull(testScope.getParent());
    }

    @Test
    public void testGetGlobalScope() {
        assertEquals(testScope, testScope.getGlobalScope());
    }

    @Test
    public void testGetParentScope() {
        assertNull(testScope.getParentScope());
    }

    @Test
    public void testGetTypeOfThis() {
        assertNull(testScope.getTypeOfThis());
    }

    @Test
    public void testDeclare() {
        Scope.Var var = testScope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
    }

    @Test
    public void testUndeclare() {
        Scope.Var var = testScope.declare("testVar", null, null, null);
        testScope.undeclare(var);
        assertNull(testScope.getVar("testVar"));
    }

    @Test
    public void testGetSlot() {
        Scope.Var var = testScope.declare("testVar", null, null, null);
        assertEquals(var, testScope.getSlot("testVar"));
    }

    @Test
    public void testGetOwnSlot() {
        Scope.Var var = testScope.declare("testVar", null, null, null);
        assertEquals(var, testScope.getOwnSlot("testVar"));
    }

    @Test
    public void testGetVar() {
        Scope.Var var = testScope.declare("testVar", null, null, null);
        assertEquals(var, testScope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        testScope.declare("testVar", null, null, null);
        assertTrue(testScope.isDeclared("testVar", true));
    }

    @Test
    public void testGetVars() {
        testScope.declare("testVar1", null, null, null);
        testScope.declare("testVar2", null, null, null);
        int count = 0;
        for (Scope.Var var : testScope.getVars()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetVarCount() {
        testScope.declare("testVar1", null, null, null);
        testScope.declare("testVar2", null, null, null);
        assertEquals(2, testScope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        assertTrue(testScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        assertFalse(testScope.isLocal());
    }

}