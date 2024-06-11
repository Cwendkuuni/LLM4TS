import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {
    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        // typical use case
        Scope.Var var = scope.declare("name", null, null, null);
        assertNotNull(var);
        assertEquals("name", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isDefine());

        // edge case: empty name
        var = scope.declare("", null, null, null);
        assertNotNull(var);
        assertEquals("", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertFalse(var.isDefine());

        // error scenario: null name
        var = scope.declare(null, null, null, null);
        assertNull(var);
    }

    @Test
    public void testUndeclare() {
        // typical use case
        Scope.Var var = scope.declare("name", null, null, null);
        assertTrue(scope.isDeclared("name", true));
        scope.undeclare(var);
        assertFalse(scope.isDeclared("name", true));

        // edge case: undeclare non-existing variable
        int varCountBefore = scope.getVarCount();
        scope.undeclare(var);
        assertEquals(varCountBefore, scope.getVarCount());

        // error scenario: undeclare null variable
        scope.undeclare(null);
        assertEquals(varCountBefore, scope.getVarCount());
    }

    @Test
    public void testGetVar() {
        // typical use case
        Scope.Var var = scope.declare("name", null, null, null);
        Scope.Var retrievedVar = scope.getVar("name");
        assertEquals(var, retrievedVar);

        // edge case: get non-existing variable
        retrievedVar = scope.getVar("non-existing");
        assertNull(retrievedVar);

        // error scenario: get variable with null name
        retrievedVar = scope.getVar(null);
        assertNull(retrievedVar);
    }

    @Test
    public void testIsDeclared() {
        // typical use case
        scope.declare("name", null, null, null);
        assertTrue(scope.isDeclared("name", true));
        assertTrue(scope.isDeclared("name", false));

        // edge case: check non-existing variable
        assertFalse(scope.isDeclared("non-existing", true));
        assertFalse(scope.isDeclared("non-existing", false));

        // error scenario: check variable with null name
        assertFalse(scope.isDeclared(null, true));
        assertFalse(scope.isDeclared(null, false));
    }
}