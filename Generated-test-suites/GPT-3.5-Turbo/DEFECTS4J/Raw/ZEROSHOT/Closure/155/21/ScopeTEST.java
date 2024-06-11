import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        // Test case for declare method
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        assert(var.getName().equals("name"));
    }

    @Test
    public void testUndeclare() {
        // Test case for undeclare method
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        scope.undeclare(var);
        Var undeclaredVar = scope.getVar("name");
        assert(undeclaredVar == null);
    }

    @Test
    public void testGetVar() {
        // Test case for getVar method
        Scope scope = new Scope(null, null);
        Var var = scope.declare("name", null, null, null);
        Var retrievedVar = scope.getVar("name");
        assert(retrievedVar == var);
    }

    @Test
    public void testGetVarNotFound() {
        // Test case for getVar method when variable is not found
        Scope scope = new Scope(null, null);
        Var retrievedVar = scope.getVar("name");
        assert(retrievedVar == null);
    }

    @Test
    public void testIsDeclared() {
        // Test case for isDeclared method
        Scope scope = new Scope(null, null);
        scope.declare("name", null, null, null);
        boolean isDeclared = scope.isDeclared("name", false);
        assert(isDeclared);
    }

    @Test
    public void testIsDeclaredNotFound() {
        // Test case for isDeclared method when variable is not found
        Scope scope = new Scope(null, null);
        boolean isDeclared = scope.isDeclared("name", false);
        assert(!isDeclared);
    }

    @Test
    public void testIsDeclaredRecurse() {
        // Test case for isDeclared method with recursive search
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        parentScope.declare("name", null, null, null);
        boolean isDeclared = childScope.isDeclared("name", true);
        assert(isDeclared);
    }

    @Test
    public void testIsDeclaredRecurseNotFound() {
        // Test case for isDeclared method with recursive search when variable is not found
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        boolean isDeclared = childScope.isDeclared("name", true);
        assert(!isDeclared);
    }

    @Test
    public void testIsGlobal() {
        // Test case for isGlobal method
        Scope scope = new Scope(null, null);
        boolean isGlobal = scope.isGlobal();
        assert(isGlobal);
    }

    @Test
    public void testIsLocal() {
        // Test case for isLocal method
        Scope scope = new Scope(null, null);
        boolean isLocal = scope.isLocal();
        assert(!isLocal);
    }
}