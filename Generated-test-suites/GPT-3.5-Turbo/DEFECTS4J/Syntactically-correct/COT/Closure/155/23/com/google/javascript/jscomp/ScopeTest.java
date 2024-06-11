
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null, true);
        assertEquals("testVar", var.getName());
        assertNull(var.getParentNode());
        assertEquals(scope, var.getScope());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
        assertNull(var.getJSDocInfo());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null, true);
        scope.undeclare(var);
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isDeclared("testVar", true));

        scope.declare("testVar", null, null, null, true);
        assertTrue(scope.isDeclared("testVar", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        Var var1 = scope.declare("testVar1", null, null, null, true);
        Var var2 = scope.declare("testVar2", null, null, null, true);

        Iterator<Var> vars = scope.getVars();
        assertTrue(vars.hasNext());
        assertEquals(var1, vars.next());
        assertTrue(vars.hasNext());
        assertEquals(var2, vars.next());
        assertFalse(vars.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", null, null, null, true);
        scope.declare("testVar2", null, null, null, true);
        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        Scope scope = new Scope(null, null);
        assertTrue(scope.isGlobal());

        Scope localScope = new Scope(scope, null);
        assertFalse(localScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isLocal());

        Scope localScope = new Scope(scope, null);
        assertTrue(localScope.isLocal());
    }

    @Test
    public void testGetDepth() {
        Scope scope = new Scope(null, null);
        assertEquals(0, scope.getDepth());

        Scope localScope = new Scope(scope, null);
        assertEquals(1, localScope.getDepth());
    }

    @Test
    public void testIsBottom() {
        Scope scope = new Scope(null, null);
        assertFalse(scope.isBottom());
    }

    @Test
    public void testGetRootNode() {
        Node rootNode = new Node(Token.ROOT);
        Scope scope = new Scope(rootNode, null);
        assertEquals(rootNode, scope.getRootNode());
    }

    @Test
    public void testGetParent() {
        Scope parentScope = new Scope(null, null);
        Node rootNode = new Node(Token.ROOT);
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testGetTypeOfThis() {
        ObjectType thisType = new ObjectType();
        Scope scope = new Scope(null, rootNode, thisType);
        assertEquals(thisType, scope.getTypeOfThis());
    }
}
