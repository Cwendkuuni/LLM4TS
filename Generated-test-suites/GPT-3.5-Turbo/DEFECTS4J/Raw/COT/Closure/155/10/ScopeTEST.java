
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isConst());
        assertFalse(var.isDefine());
        assertFalse(var.isGlobal());
        assertTrue(var.isLocal());
        assertFalse(var.isNoShadow());
        assertFalse(var.isTypeInferred());
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
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        scope.declare("testVar", null, null, null);
        assertTrue(scope.isDeclared("testVar", false));
        assertFalse(scope.isDeclared("nonExistentVar", false));
    }

}
