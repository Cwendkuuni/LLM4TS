import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);
        
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals("testVar", var.getInputName());
        assertFalse(var.isConst());
        assertFalse(var.isDefine());
        assertFalse(var.isExtern());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
        assertFalse(var.isNoShadow());
        assertFalse(var.isTypeInferred());
        assertNull(var.getInitialValue());
        assertNull(var.getParentNode());
        assertNull(var.getType());
        assertNull(var.getJSDocInfo());
        assertNull(var.getNameNode());
    }

    @Test
    public void testUndeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);
        
        scope.undeclare(var);
        
        assertNull(scope.getVar("testVar"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", new Node(Token.NAME, "testVar"), new JSType(), null);
        
        assertTrue(scope.isDeclared("testVar", false));
        assertTrue(scope.isDeclared("testVar", true));

        assertFalse(scope.isDeclared("nonExistentVar", false));
        assertFalse(scope.isDeclared("nonExistentVar", true));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar1", new Node(Token.NAME, "testVar1"), new JSType(), null);
        scope.declare("testVar2", new Node(Token.NAME, "testVar2"), new JSType(), null);

        Iterator<Var> varIterator = scope.getVars();
        List<String> varNames = new ArrayList<>();
        while (varIterator.hasNext()) {
            varNames.add(varIterator.next().getName());
        }

        assertTrue(varNames.contains("testVar1"));
        assertTrue(varNames.contains("testVar2"));
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
}