import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var", null, null, null);
        assertEquals("var", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertNull(var.getInputName());
        assertNull(var.getJSDocInfo());
        assertNull(var.getInitialValue());
        assertNull(var.getScope());
        assertNull(var.getParentNode());
        assertEquals(false, var.isBleedingFunction());
        assertEquals(false, var.isGlobal());
        assertEquals(true, var.isLocal());
        assertEquals(false, var.isExtern());
        assertEquals(false, var.isConst());
        assertEquals(false, var.isDefine());
        assertEquals(true, var.isTypeInferred());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("var", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("var"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        assertEquals(true, scope.isDeclared("var1", true));
        assertEquals(false, scope.isDeclared("var2", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("var1", null, null, null);
        scope.declare("var2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertEquals(true, scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(new Scope(null, null), null);
        assertEquals(true, scope.isLocal());
    }
}