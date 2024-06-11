
import org.junit.Test;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var", null, null, null);
        assertEquals("var", var.getName());
        assertNull(var.getParentNode());
        assertFalse(var.isBleedingFunction());
        assertEquals(scope, var.getScope());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("var"));
    }

    // Add more test cases for other methods
}
