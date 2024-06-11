import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.UNKNOWN, var.getType());
    }

    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVar() {
        Scope parent = new Scope(null, new Node(Token.FUNCTION));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION));
        Var var = parent.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertEquals(var, scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope parent = new Scope(null, new Node(Token.FUNCTION));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION));
        parent.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertTrue(scope.isDeclared("testVar", true));
    }

    @Test
    public void testGlobalScope() {
        Scope globalScope = new Scope(new Node(Token.BLOCK), null);
        assertTrue(globalScope.isGlobal());
        assertFalse(globalScope.isLocal());
    }

    @Test
    public void testLocalScope() {
        Scope parent = new Scope(null, new Node(Token.FUNCTION));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION));
        assertFalse(scope.isGlobal());
        assertTrue(scope.isLocal());
    }

    @Test
    public void testDeclareVarTwice() {
        Scope scope = new Scope(null, new Node(Token.FUNCTION));
        Var var1 = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        Var var2 = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
        assertNotEquals(var1, var2);
    }

    @Test
    public void testBottomScope() {
        Scope scope = new Scope(new Node(Token.FUNCTION), JSType.NULL);
        assertTrue(scope.isBottom());
    }

}