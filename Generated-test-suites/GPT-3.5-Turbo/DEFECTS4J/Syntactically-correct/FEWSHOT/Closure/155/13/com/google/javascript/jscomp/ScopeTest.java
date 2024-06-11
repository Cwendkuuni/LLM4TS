import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope parent = new Scope(null, TestUtils.createNode(Token.FUNCTION));
        Scope scope = new Scope(parent, TestUtils.createNode(Token.BLOCK));

        Var var1 = scope.declare("x", TestUtils.createNode(Token.NAME), null, null);
        Var var2 = scope.declare("y", TestUtils.createNode(Token.NAME), null, null);

        assertEquals(var1, scope.getVar("x"));
        assertEquals(var2, scope.getVar("y"));
    }

    @Test
    public void testUndeclare() {
        Scope parent = new Scope(null, TestUtils.createNode(Token.FUNCTION));
        Scope scope = new Scope(parent, TestUtils.createNode(Token.BLOCK));

        Var var1 = scope.declare("x", TestUtils.createNode(Token.NAME), null, null);
        Var var2 = scope.declare("y", TestUtils.createNode(Token.NAME), null, null);

        assertEquals(var1, scope.getVar("x"));
        assertEquals(var2, scope.getVar("y"));

        scope.undeclare(var1);

        assertNull(scope.getVar("x"));
        assertEquals(var2, scope.getVar("y"));
    }
}