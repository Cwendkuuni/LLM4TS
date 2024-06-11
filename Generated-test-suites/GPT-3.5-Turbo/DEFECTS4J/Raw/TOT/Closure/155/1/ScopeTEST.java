import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    private Scope scope;

    @Before
    public void setUp() {
        // Create a global scope for testing
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclareVariable() {
        String name = "x";
        Node nameNode = new Node(Token.NAME, "x");
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");
        
        Var var = scope.declare(name, nameNode, type, input);
        
        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(input, var.getInput());
        assertFalse(var.isInferred());
    }

    @Test
    public void testUndeclareVariable() {
        String name = "x";
        Node nameNode = new Node(Token.NAME, "x");
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, type, input);
        assertTrue(scope.isDeclared(name, true));

        scope.undeclare(var);
        assertFalse(scope.isDeclared(name, true));
    }

    @Test
    public void testGetVariable() {
        String name = "x";
        Node nameNode = new Node(Token.NAME, "x");
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, type, input);

        Var retrievedVar = scope.getVar(name);

        assertEquals(var, retrievedVar);
    }

    @Test
    public void testIsDeclaredTrue() {
        String name = "x";
        Node nameNode = new Node(Token.NAME, "x");
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");

        scope.declare(name, nameNode, type, input);

        assertTrue(scope.isDeclared(name, true));
    }

    @Test
    public void testIsDeclaredFalse() {
        assertFalse(scope.isDeclared("x", true));
    }

}