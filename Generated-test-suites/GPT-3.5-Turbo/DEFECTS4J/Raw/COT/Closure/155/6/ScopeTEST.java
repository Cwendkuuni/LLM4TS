
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("x", null, null, null);
        assertEquals(var.getName(), "x");
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertEquals(var.getScope(), scope);
        assertEquals(var.getIndex(), 0);
        assertNull(var.getInput());
        assertFalse(var.isDefine());
        assertNull(var.getJSDocInfo());
        assertFalse(var.isTypeInferred());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("x", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("x"));
    }

    @Test
    public void testGetSlot() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("x", null, null, null);
        assertSame(scope.getSlot("x"), var);
        assertNull(scope.getSlot("y"));
    }

    @Test
    public void testGetOwnSlot() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("x", null, null, null);
        assertSame(scope.getOwnSlot("x"), var);
        assertNull(scope.getOwnSlot("y"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("x", null, null, null);
        assertTrue(scope.isDeclared("x", true));
        assertFalse(scope.isDeclared("y", true));
        assertFalse(scope.isDeclared("y", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        Var var1 = scope.declare("x", null, null, null);
        Var var2 = scope.declare("y", null, null, null);
        Var var3 = scope.declare("z", null, null, null);
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        assertSame(iterator.next(), var1);
        assertTrue(iterator.hasNext());
        assertSame(iterator.next(), var2);
        assertTrue(iterator.hasNext());
        assertSame(iterator.next(), var3);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("x", null, null, null);
        scope.declare("y", null, null, null);
        scope.declare("z", null, null, null);
        assertEquals(scope.getVarCount(), 3);
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());

        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isLocal());

        Scope childScope = new Scope(scope, null);
        assertTrue(childScope.isLocal());
    }
}
