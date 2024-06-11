import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(var);
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        Var var = scope.getVar("testVar");
        assertNotNull(var);
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", false));
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        scope.declare("testVar1", new Node(Token.NAME), JSType.UNKNOWN, null);
        scope.declare("testVar2", new Node(Token.NAME), JSType.STRING, null);
        assertEquals(2, scope.getVarCount());
    }
}