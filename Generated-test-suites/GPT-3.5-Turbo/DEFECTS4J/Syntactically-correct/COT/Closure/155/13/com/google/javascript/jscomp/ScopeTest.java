
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertEquals("name", var.getName());
        assertNull(var.getParentNode());
        assertFalse(var.isBleedingFunction());
        assertEquals(scope, var.getScope());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
        assertFalse(var.isExtern());
        assertFalse(var.isConst());
        assertFalse(var.isDefine());
        assertNull(var.getInitialValue());
        assertNull(var.getType());
        assertNull(var.getNameNode());
        assertNull(var.getJSDocInfo());
        assertFalse(var.isTypeInferred());
        assertEquals("<non-file>", var.getInputName());
        assertFalse(var.isNoShadow());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("name"));
    }

    @Test
    public void testGetVar() {
        Scope scope1 = new Scope(null, null);
        Scope scope2 = new Scope(scope1, null);

        Var var1 = scope1.declare("name1", null, null, null);
        Var var2 = scope2.declare("name2", null, null, null);

        assertEquals(var1, scope1.getVar("name1"));
        assertNull(scope1.getVar("name2"));
        assertEquals(var2, scope2.getVar("name2"));
        assertEquals(var1, scope2.getVar("name1"));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        Var var1 = scope.declare("name1", null, null, null);
        Var var2 = scope.declare("name2", null, null, null);
        Var var3 = scope.declare("name3", null, null, null);

        Iterator<Scope.Var> iterator = scope.getVars();
        assertEquals(var1, iterator.next());
        assertEquals(var2, iterator.next());
        assertEquals(var3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsDeclared() {
        Scope scope1 = new Scope(null, null);
        Scope scope2 = new Scope(scope1, null);

        scope1.declare("name1", null, null, null);
        scope2.declare("name2", null, null, null);

        assertTrue(scope1.isDeclared("name1", true));
        assertFalse(scope1.isDeclared("name2", true));
        assertTrue(scope2.isDeclared("name1", true));
        assertTrue(scope2.isDeclared("name2", true));
    }

    @Test
    public void testIsGlobal() {
        Scope scope1 = new Scope(null, null);
        Scope scope2 = new Scope(scope1, null);

        assertTrue(scope1.isGlobal());
        assertFalse(scope2.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope1 = new Scope(null, null);
        Scope scope2 = new Scope(scope1, null);

        assertFalse(scope1.isLocal());
        assertTrue(scope2.isLocal());
    }
}
