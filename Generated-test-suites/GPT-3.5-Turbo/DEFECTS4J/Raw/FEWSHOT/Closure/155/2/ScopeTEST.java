import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopeTest {
    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertEquals("testVar", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertNull(var.getJSDocInfo());
        assertNull(var.getInputName());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetVar() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        Var var1 = parentScope.declare("testVar1", null, null, null);
        Var var2 = childScope.declare("testVar2", null, null, null);
        assertEquals(var1, parentScope.getVar("testVar1"));
        assertEquals(var2, childScope.getVar("testVar2"));
        assertNull(childScope.getVar("testVar1"));
    }

    @Test
    public void testIsDeclared() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        parentScope.declare("testVar1", null, null, null);
        childScope.declare("testVar2", null, null, null);
        assertEquals(true, parentScope.isDeclared("testVar1", false));
        assertEquals(true, childScope.isDeclared("testVar2", false));
        assertEquals(true, childScope.isDeclared("testVar1", true));
        assertEquals(false, parentScope.isDeclared("testVar2", false));
    }

    @Test
    public void testIsGlobal() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        assertEquals(true, parentScope.isGlobal());
        assertEquals(false, childScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        assertEquals(false, parentScope.isLocal());
        assertEquals(true, childScope.isLocal());
    }
}