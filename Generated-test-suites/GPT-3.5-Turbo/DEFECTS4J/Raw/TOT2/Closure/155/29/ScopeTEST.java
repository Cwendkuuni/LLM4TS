import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVariable() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.UNKNOWN, var.getType());
    }

    @Test
    public void testUndeclareVariable() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
        scope.undeclare(var);
        Var undeclaredVar = scope.getVar("testVar");
        assertNull(undeclaredVar);
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", false));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.UNKNOWN, null);
        Var retrievedVar = scope.getVar("testVar");
        assertNotNull(retrievedVar);
        assertEquals(var, retrievedVar);
    }
}