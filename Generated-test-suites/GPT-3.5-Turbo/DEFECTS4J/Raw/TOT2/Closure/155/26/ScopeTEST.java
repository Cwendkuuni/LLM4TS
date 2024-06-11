import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);

        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(new Node(Token.NAME, "testVar"), var.getNameNode());
        assertEquals(new JSType(), var.getType());
    }

    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);

        scope.undeclare(var);

        assertNull(scope.getVar("testVar"));
    }

    // Add more test cases for other methods in the Scope class

}