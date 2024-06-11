import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);

        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(new Node(Token.NAME, "testVar"), var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(true, var.isTypeInferred());
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);

        Var retrievedVar = scope.getVar("testVar");

        assertNotNull(retrievedVar);
        assertEquals(var, retrievedVar);
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);

        boolean isDeclared = scope.isDeclared("testVar", false);

        assertTrue(isDeclared);
    }

    @Test
    public void testIsGlobalScope() {
        Scope globalScope = new Scope(new Node(Token.BLOCK), new JSType());
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

        assertTrue(globalScope.isGlobal());
        assertFalse(localScope.isGlobal());
    }

    @Test
    public void testIsLocalScope() {
        Scope globalScope = new Scope(new Node(Token.BLOCK), new JSType());
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

        assertFalse(globalScope.isLocal());
        assertTrue(localScope.isLocal());
    }

}