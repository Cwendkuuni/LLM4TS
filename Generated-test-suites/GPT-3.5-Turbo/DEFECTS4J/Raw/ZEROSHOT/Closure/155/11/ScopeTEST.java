import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        Var var = scope.declare("testName", null, null, null);

        Assert.assertEquals("testName", var.getName());
        Assert.assertNull(var.getNameNode());
        Assert.assertNull(var.getType());
        Assert.assertFalse(var.isInferred());
    }

    @Test
    public void testUndeclare() {
        Var var = scope.declare("testName", null, null, null);
        boolean result = scope.undeclare(var);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsDeclared() {
        Var var = scope.declare("testName", null, null, null);
        boolean result = scope.isDeclared("testName", false);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetVars() {
        Var var1 = scope.declare("testName1", null, null, null);
        Var var2 = scope.declare("testName2", null, null, null);

        Iterator<Var> iterator = scope.getVars();
        Assert.assertEquals(var1, iterator.next());
        Assert.assertEquals(var2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        scope.declare("testName1", null, null, null);
        scope.declare("testName2", null, null, null);

        int count = scope.getVarCount();
        Assert.assertEquals(2, count);
    }

    @Test
    public void testIsGlobal() {
        boolean result = scope.isGlobal();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsLocal() {
        boolean result = scope.isLocal();

        Assert.assertFalse(result);
    }
}