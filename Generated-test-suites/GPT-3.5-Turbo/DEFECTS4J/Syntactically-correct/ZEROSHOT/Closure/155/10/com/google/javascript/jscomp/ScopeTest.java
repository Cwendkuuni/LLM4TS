import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeTest {

    // Test declare method
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertTrue(var.isLocal());
    }

    // Test undeclare method
    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", true));
        scope.undeclare(var);
        assertFalse(scope.isDeclared("testVar", true));
    }

    // Test getVar method
    @Test
    public void testGetVar() {
        Scope parentScope = new Scope(null, null);
        Scope scope = new Scope(parentScope, null);
        Var var = parentScope.declare("testVar", null, null, null);
        assertNotNull(scope.getVar("testVar"));
        assertEquals(var, scope.getVar("testVar"));
    }

    // Test isDeclared method
    @Test
    public void testIsDeclared() {
        Scope parentScope = new Scope(null, null);
        Scope scope = new Scope(parentScope, null);
        parentScope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", true));
    }

    // Test getVars method
    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);
        scope.declare("var3", null, null, null);
        assertEquals(3, scope.getVarCount());
        Iterator<Var> iterator = scope.getVars();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }

    // Test isGlobal method
    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());
        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isGlobal());
    }

    // Test isLocal method
    @Test
    public void testIsLocal() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isLocal());
        Scope childScope = new Scope(scope, null);
        assertTrue(childScope.isLocal());
    }
}