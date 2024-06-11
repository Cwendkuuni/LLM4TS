
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        String name = "testVar";
        Node nameNode = new Node(Token.NAME);
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();

        Var var = scope.declare(name, nameNode, type, input);
        assertNotNull(var);
        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(input, var.getInput());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        String name = "testVar";
        Node nameNode = new Node(Token.NAME);
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();

        Var var = scope.declare(name, nameNode, type, input);
        scope.undeclare(var);
        assertNull(scope.getVar(name));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        String name = "testVar";
        Node nameNode = new Node(Token.NAME);
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();

        assertFalse(scope.isDeclared(name, false));
        assertFalse(scope.isDeclared(name, true));

        Var var = scope.declare(name, nameNode, type, input);

        assertTrue(scope.isDeclared(name, false));
        assertTrue(scope.isDeclared(name, true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);

        Iterator<Var> varsIterator = scope.getVars();
        assertFalse(varsIterator.hasNext());

        String name = "testVar";
        Node nameNode = new Node(Token.NAME);
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        scope.declare(name, nameNode, type, input);

        varsIterator = scope.getVars();
        assertTrue(varsIterator.hasNext());
        assertEquals(name, varsIterator.next().getName());
        assertFalse(varsIterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        assertEquals(0, scope.getVarCount());

        String name = "testVar";
        Node nameNode = new Node(Token.NAME);
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        scope.declare(name, nameNode, type, input);

        assertEquals(1, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope1 = new Scope(null, null);
        assertTrue(scope1.isGlobal());

        Scope scope2 = new Scope(scope1, null);
        assertFalse(scope2.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope1 = new Scope(null, null);
        assertFalse(scope1.isLocal());

        Scope scope2 = new Scope(scope1, null);
        assertTrue(scope2.isLocal());
    }
}
