import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        Var var = scope.declare("variable", null, null);
        assertNotNull(var);
        assertEquals("variable", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertNull(var.getJSDocInfo());
        assertFalse(var.isTypeInferred());
    }

    @Test
    public void testUndeclare() {
        Var var = scope.declare("variable", null, null);
        assertNotNull(var);
        scope.undeclare(var);
        assertNull(scope.getVar("variable"));
    }

    @Test
    public void testGetVar() {
        Var var1 = scope.declare("variable1", null, null);
        assertNotNull(var1);
        Var var2 = new Var(false, "variable2", null, null, null, 0, null, false, null);
        scope.vars.put("variable2", var2);
        assertEquals(var1, scope.getVar("variable1"));
        assertEquals(var2, scope.getVar("variable2"));
        assertNull(scope.getVar("variable3"));
    }

    @Test
    public void testIsDeclared() {
        scope.declare("variable1", null, null);
        scope.vars.put("variable2", new Var(false, "variable2", null, null, null, 0, null, false, null));
        assertTrue(scope.isDeclared("variable1", false));
        assertTrue(scope.isDeclared("variable2", false));
        assertFalse(scope.isDeclared("variable3", false));
    }

    @Test
    public void testIsGlobal() {
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        assertFalse(scope.isLocal());
    }

    @Test
    public void testGetVars() {
        scope.vars.put("variable1", new Var(false, "variable1", null, null, null, 0, null, false, null));
        scope.vars.put("variable2", new Var(false, "variable2", null, null, null, 0, null, false, null));
        scope.vars.put("variable3", new Var(false, "variable3", null, null, null, 0, null, false, null));
        assertEquals(3, scope.getVarCount());
    }

    @Test
    public void testGetTypeOfThis() {
        assertNull(scope.getTypeOfThis());
    }

}