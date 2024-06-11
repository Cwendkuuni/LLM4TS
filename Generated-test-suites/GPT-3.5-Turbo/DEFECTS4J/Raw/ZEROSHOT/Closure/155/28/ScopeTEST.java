import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertEquals("testVar", var.getName());
        assertNull(var.getType());
        assertFalse(var.isTypeInferred());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertTrue(scope.vars.containsKey("testVar"));
        scope.undeclare(var);
        assertFalse(scope.vars.containsKey("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null);
        scope.declare("testVar2", null, null, null);
        assertTrue(scope.isDeclared("testVar1", false));
        assertTrue(scope.isDeclared("testVar2", false));
        assertFalse(scope.isDeclared("testVar3", false));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null);
        scope.declare("testVar2", null, null, null);
        Var var1 = scope.getVar("testVar1");
        Var var2 = scope.getVar("testVar2");
        assertEquals("testVar1", var1.getName());
        assertEquals("testVar2", var2.getName());
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null);
        scope.declare("testVar2", null, null, null);
        int count = 0;
        for (Var var : scope.getVars()) {
            count++;
            assertNotNull(var);
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null);
        scope.declare("testVar2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope1 = new Scope(null, null);
        assertTrue(scope1.isGlobal());

        Scope scope2 = new Scope(scope1, null);
        assertFalse(scope2.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope1 = new Scope(null, null);
        assertFalse(scope1.isLocal());

        Scope scope2 = new Scope(scope1, null);
        assertTrue(scope2.isLocal());
    }

    @Test
    public void testGetTypeOfThis() {
        Scope scope = new Scope(null, null);
        assertNull(scope.getTypeOfThis());
    }
}