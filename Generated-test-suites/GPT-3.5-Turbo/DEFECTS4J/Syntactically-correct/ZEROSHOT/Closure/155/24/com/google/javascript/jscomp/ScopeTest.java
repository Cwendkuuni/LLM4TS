import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("test", null, null, null);
        assertEquals("test", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isTypeInferred());
        assertFalse(var.isDefine());
        assertNull(var.getJSDocInfo());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("test", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("test"));
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Var var1 = scope.declare("test1", null, null, null);
        Var var2 = scope.declare("test2", null, null, null);
        assertEquals(var1, scope.getVar("test1"));
        assertEquals(var2, scope.getVar("test2"));
        assertNull(scope.getVar("test3"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isDeclared("test1", false));
        scope.declare("test1", null, null, null);
        assertTrue(scope.isDeclared("test1", false));
        assertFalse(scope.isDeclared("test2", false));
        assertFalse(scope.isDeclared("test2", true));
    }
}