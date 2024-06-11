import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertNull(var.getType());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(scope.getVar("testVar"));
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(scope.getVar("testVar"));
        assertNull(scope.getVar("nonExistent"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", false));
        assertFalse(scope.isDeclared("nonExistent", false));
    }

    @Test
    public void testIsGlobal() {
        Scope globalScope = new Scope(null, null);
        assertTrue(globalScope.isGlobal());

        Scope localScope = new Scope(globalScope, null);
        assertFalse(localScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope globalScope = new Scope(null, null);
        assertFalse(globalScope.isLocal());

        Scope localScope = new Scope(globalScope, null);
        assertTrue(localScope.isLocal());
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);

        int count = 0;
        for (Var var : scope.getVars()) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);

        assertEquals(2, scope.getVarCount());
    }
}