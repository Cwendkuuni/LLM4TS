import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertEquals("testVar", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getInputName());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", true));
        assertFalse(scope.isDeclared("nonexistentVar", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);
        scope.declare("var3", null, null, null);
        int count = 0;
        for (Var var : scope.getVars()) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);
        scope.declare("var3", null, null, null);
        assertEquals(3, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope parent = new Scope(null, null);
        Scope child = new Scope(parent, null);
        assertTrue(child.isLocal());
        assertFalse(parent.isLocal());
    }
}