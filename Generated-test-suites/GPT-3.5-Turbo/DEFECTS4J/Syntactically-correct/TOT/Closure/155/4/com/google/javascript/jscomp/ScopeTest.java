import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("myVar", null, null, null);

        assertNotNull(var);
        assertEquals("myVar", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isTypeInferred());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("myVar", null, null, null);

        assertNotNull(var);
        assertEquals("myVar", var.getName());

        scope.undeclare(var);
        assertNull(scope.getVar("myVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("myVar", null, null, null);

        assertTrue(scope.isDeclared("myVar", true));
        assertFalse(scope.isDeclared("anotherVar", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        Var var1 = scope.declare("var1", null, null, null);
        Var var2 = scope.declare("var2", null, null, null);

        Iterator<Var> it = scope.getVars();

        assertTrue(it.hasNext());
        assertEquals(var1, it.next());

        assertTrue(it.hasNext());
        assertEquals(var2, it.next());

        assertFalse(it.hasNext());
    }

    @Test
    public void testIsGlobal() {
        Scope globalScope = new Scope(null, null);
        Scope localScope = new Scope(globalScope, null);

        assertTrue(globalScope.isGlobal());
        assertFalse(localScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope globalScope = new Scope(null, null);
        Scope localScope = new Scope(globalScope, null);

        assertFalse(globalScope.isLocal());
        assertTrue(localScope.isLocal());
    }

    @Test
    public void testGetTypeOfThis() {
        ObjectType objectType = new ObjectType();
        Scope scope = new Scope(null, objectType);

        assertEquals(objectType, scope.getTypeOfThis());
    }

    @Test
    public void testGetParentScope() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);

        assertEquals(parentScope, childScope.getParentScope());
    }

}