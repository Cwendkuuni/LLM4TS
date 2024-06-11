import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        // Create a scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));

        // Declare a variable
        Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
        Assert.assertNotNull(var);
    }

    @Test
    public void testUndeclare() {
        // Create a scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));

        // Declare a variable
        Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
        Assert.assertNotNull(var);

        // Undeclare the variable
        scope.undeclare(var);

        // Verify the variable is no longer declared
        Var undeclaredVar = scope.getVar("testVar");
        Assert.assertNull(undeclaredVar);
    }

    @Test
    public void testIsDeclared() {
        // Create a scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));

        // Declare a variable
        Var var = scope.declare("testVar", new Node(Token.NAME), null, null);
        Assert.assertNotNull(var);

        // Check if the variable is declared (recurse = true)
        boolean isDeclared = scope.isDeclared("testVar", true);
        Assert.assertTrue(isDeclared);

        // Check if the variable is declared (recurse = false)
        boolean isDeclaredNotRecursed = scope.isDeclared("testVar", false);
        Assert.assertTrue(isDeclaredNotRecursed);
    }

    @Test
    public void testGetVars() {
        // Create a scope
        Scope scope = new Scope(null, new Node(Token.FUNCTION));

        // Declare some variables
        Var var1 = scope.declare("var1", new Node(Token.NAME), null, null);
        Var var2 = scope.declare("var2", new Node(Token.NAME), null, null);
        Var var3 = scope.declare("var3", new Node(Token.NAME), null, null);

        // Get all variables in the scope
        Iterator<Var> varIterator = scope.getVars();

        // Check if all variables are retrieved
        Assert.assertEquals(var1, varIterator.next());
        Assert.assertEquals(var2, varIterator.next());
        Assert.assertEquals(var3, varIterator.next());
        Assert.assertFalse(varIterator.hasNext());
    }

    @Test
    public void testIsGlobal() {
        // Create a global scope
        Scope globalScope = new Scope(new Node(Token.SCRIPT), null);
        Assert.assertTrue(globalScope.isGlobal());

        // Create a local scope
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
        Assert.assertFalse(localScope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        // Create a global scope
        Scope globalScope = new Scope(new Node(Token.SCRIPT), null);
        Assert.assertFalse(globalScope.isLocal());

        // Create a local scope
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
        Assert.assertTrue(localScope.isLocal());
    }

    @Test
    public void testGetTypeOfThis() {
        // Create a global scope with a specific this type
        ObjectType thisType = new ObjectType();
        Scope globalScope = new Scope(new Node(Token.SCRIPT), thisType);
        Assert.assertEquals(thisType, globalScope.getTypeOfThis());

        // Create a local scope with a specific this type
        Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
        Assert.assertEquals(thisType, localScope.getTypeOfThis());
    }
}