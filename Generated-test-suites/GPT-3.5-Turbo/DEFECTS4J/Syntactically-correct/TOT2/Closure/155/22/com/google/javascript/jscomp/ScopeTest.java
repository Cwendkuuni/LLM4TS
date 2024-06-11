import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        // Test declaring a variable in the scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.STRING, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.STRING, var.getType());
    }

    @Test
    public void testUndeclareVar() {
        // Test undeclaring a variable from the scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.STRING, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        // Test checking if a variable is declared in the scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("testVar", new Node(Token.NAME), JSType.STRING, null);
        assertTrue(scope.isDeclared("testVar", false));
        assertFalse(scope.isDeclared("nonExistentVar", false));
    }

    @Test
    public void testGetVarCount() {
        // Test getting the count of variables in the scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("var1", new Node(Token.NAME), JSType.NUMBER, null);
        scope.declare("var2", new Node(Token.NAME), JSType.BOOLEAN, null);
        assertEquals(2, scope.getVarCount());
    }

}