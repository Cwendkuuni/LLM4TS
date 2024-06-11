import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Assert;
import org.junit.Test;

public class ScopeTest {

    // Create a test instance of the Scope class
    Scope scope = new Scope();

    @Test
    public void testDeclare() {
        String name = "variable";
        Node nameNode = new Node(Token.NAME, "variable");
        JSType type = JSType.UNKNOWN;
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, type, input);

        Assert.assertNotNull(var);
        Assert.assertEquals(name, var.getName());
        Assert.assertEquals(nameNode, var.getNameNode());
        Assert.assertEquals(type, var.getType());
        Assert.assertEquals(input, var.getInput());
    }

    @Test
    public void testGetVar() {
        String name = "variable";
        Node nameNode = new Node(Token.NAME, "variable");
        JSType type = JSType.UNKNOWN;
        CompilerInput input = new CompilerInput("test.js");

        scope.declare(name, nameNode, type, input);

        Var var = scope.getVar(name);

        Assert.assertNotNull(var);
        Assert.assertEquals(name, var.getName());
        Assert.assertEquals(nameNode, var.getNameNode());
        Assert.assertEquals(type, var.getType());
        Assert.assertEquals(input, var.getInput());
    }

    @Test
    public void testIsDeclared() {
        String name = "variable";
        Node nameNode = new Node(Token.NAME, "variable");
        JSType type = JSType.UNKNOWN;
        CompilerInput input = new CompilerInput("test.js");

        scope.declare(name, nameNode, type, input);

        boolean isDeclared = scope.isDeclared(name, true);

        Assert.assertTrue(isDeclared);
    }

    @Test
    public void testGetVarCount() {
        String name1 = "variable1";
        Node nameNode1 = new Node(Token.NAME, "variable1");
        JSType type1 = JSType.UNKNOWN;
        CompilerInput input1 = new CompilerInput("test1.js");

        String name2 = "variable2";
        Node nameNode2 = new Node(Token.NAME, "variable2");
        JSType type2 = JSType.UNKNOWN;
        CompilerInput input2 = new CompilerInput("test2.js");

        scope.declare(name1, nameNode1, type1, input1);
        scope.declare(name2, nameNode2, type2, input2);

        int varCount = scope.getVarCount();

        Assert.assertEquals(2, varCount);
    }

    @Test
    public void testIsGlobal() {
        boolean isGlobal = scope.isGlobal();

        Assert.assertTrue(isGlobal);
    }

    @Test
    public void testIsLocal() {
        boolean isLocal = scope.isLocal();

        Assert.assertFalse(isLocal);
    }
}