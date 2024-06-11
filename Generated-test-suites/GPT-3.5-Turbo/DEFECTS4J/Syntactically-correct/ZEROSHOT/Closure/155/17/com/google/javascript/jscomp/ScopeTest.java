import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, rootNode);
        Var var = scope.declare("testVar", nameNode, type, input);
        assertTrue(scope.isDeclared("testVar", true));
        assertEquals(var, scope.getVar("testVar"));
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, rootNode);
        Var var = scope.declare("testVar", nameNode, type, input);
        scope.undeclare(var);
        assertFalse(scope.isDeclared("testVar", true));
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testGetGlobalScope() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        Scope globalScope = scope.getGlobalScope();
        assertNull(parent.getParent());
        assertEquals(parent, globalScope);
    }

    @Test
    public void testGetParent() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        assertEquals(parent, scope.getParent());
    }

    @Test
    public void testGetVar() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        Var var = parent.declare("testVar", nameNode, type, input);
        assertEquals(var, scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        parent.declare("testVar", nameNode, type, input);
        assertTrue(scope.isDeclared("testVar", true));
        assertFalse(scope.isDeclared("testVar2", true));
    }

    @Test
    public void testGetVars() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        parent.declare("testVar1", nameNode, type, input);
        parent.declare("testVar2", nameNode, type, input);
        parent.declare("testVar3", nameNode, type, input);
        assertEquals(3, parent.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, rootNode);
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope parent = new Scope(null, rootNode);
        Scope scope = new Scope(parent, rootNode);
        assertTrue(scope.isLocal());
    }
}