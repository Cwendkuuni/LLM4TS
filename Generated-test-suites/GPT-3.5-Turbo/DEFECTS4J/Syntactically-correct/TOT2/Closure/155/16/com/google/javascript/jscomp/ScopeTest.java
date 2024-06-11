import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.UNKNOWN, var.getType());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(scope.getVar("testVar"));
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", false));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertEquals(var, scope.getVar("testVar"));
    }
}