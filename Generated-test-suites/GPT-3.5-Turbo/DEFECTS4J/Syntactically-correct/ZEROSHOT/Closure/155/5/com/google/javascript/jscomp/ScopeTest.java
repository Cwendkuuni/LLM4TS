import org.junit.Test;
import org.junit.Assert;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var1", null, null, null);
        Assert.assertEquals("var1", var.getName());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var1", null, null, null);
        scope.undeclare(var);
        Assert.assertNull(scope.getVar("var1"));
    }

    @Test
    public void testGetVar() {
        Scope scope1 = new Scope(null, null);
        Var var = scope1.declare("var1", null, null, null);
        Scope scope2 = new Scope(scope1, null);
        Assert.assertEquals(var, scope2.getVar("var1"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope1 = new Scope(null, null);
        scope1.declare("var1", null, null, null);
        Scope scope2 = new Scope(scope1, null);
        Assert.assertTrue(scope2.isDeclared("var1", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);

        Iterator<Var> varsIterator = scope.getVars();
        Assert.assertEquals("var1", varsIterator.next().getName());
        Assert.assertEquals("var2", varsIterator.next().getName());
        Assert.assertFalse(varsIterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);
        Assert.assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        Assert.assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(new Scope(null, null), null);
        Assert.assertTrue(scope.isLocal());
    }
}