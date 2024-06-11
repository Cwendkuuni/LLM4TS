import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertEquals(var.getName(), "name");
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("name"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isDeclared("name", false));
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertTrue(scope.isDeclared("name", false));
    }
}