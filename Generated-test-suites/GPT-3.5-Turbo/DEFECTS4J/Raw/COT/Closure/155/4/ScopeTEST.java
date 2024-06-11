import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("test", null, null, null);

        assertEquals("test", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isTypeInferred());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("test", null, null, null);

        scope.undeclare(var);
        assertNull(scope.getVar("test"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("test", null, null, null);

        assertTrue(scope.isDeclared("test", true));
        assertTrue(scope.isDeclared("test", false));
        assertFalse(scope.isDeclared("other", true));
        assertFalse(scope.isDeclared("other", false));

        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isDeclared("test", true));
        assertTrue(childScope.isDeclared("test", false));
        assertFalse(childScope.isDeclared("other", true));
        assertFalse(childScope.isDeclared("other", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);

        Iterator<Var> vars = scope.getVars();
        assertTrue(vars.hasNext());
        assertEquals("var1", vars.next().getName());
        assertTrue(vars.hasNext());
        assertEquals("var2", vars.next().getName());
        assertFalse(vars.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);

        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());

        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isLocal());

        Scope childScope = new Scope(scope, null);
        assertTrue(childScope.isLocal());
    }
}