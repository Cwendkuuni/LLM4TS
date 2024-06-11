import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
    }

    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null);
        scope.declare("testVar2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobalScope() {
        Scope globalScope = new Scope(null, null);
        assertTrue(globalScope.isGlobal());
    }

    @Test
    public void testIsLocalScope() {
        Scope parentScope = new Scope(null, null);
        Scope localScope = new Scope(parentScope, null);
        assertTrue(localScope.isLocal());
    }

}