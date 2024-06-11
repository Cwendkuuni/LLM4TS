import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclareVariable() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals("testVar", var.getNameNode().getString());
        assertEquals(JSType.STRING, var.getType());
    }

    @Test
    public void testUndeclareVariable() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        assertNotNull(scope.getVar("testVar"));
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVariable() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        Var retrievedVar = scope.getVar("testVar");
        assertNotNull(retrievedVar);
        assertEquals(var, retrievedVar);
    }

    // Add more test cases for other methods in the Scope class

}