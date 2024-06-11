import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Var var = scope.declare("test", null, null, null);
        assertEquals("test", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertNull(var.getInitialValue());
        assertNull(var.getJSDocInfo());
    }

    @Test
    public void testUndeclare() {
        Var var = scope.declare("test", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("test"));
    }

    @Test
    public void testGetVar() {
        Var var = scope.declare("test", null, null, null);
        assertEquals(var, scope.getVar("test"));
    }

    @Test
    public void testIsDeclared() {
        scope.declare("test", null, null, null);
        assertEquals(true, scope.isDeclared("test", true));
        assertEquals(false, scope.isDeclared("test2", true));
        assertEquals(false, scope.isDeclared("test2", false));
    }

    @Test
    public void testIsGlobal() {
        assertEquals(true, scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        assertEquals(false, scope.isLocal());
    }
}