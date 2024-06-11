import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;
import java.util.List;

public class RenameVarsTest {

  private static Node getSimpleNode(String nodeType, String name) {
    Node node = NodeFactory.createNode(nodeType);
    node.setString(name);
    return node;
  }

  private static Node getCallNode(String nodeType, String functionName,
      List<Node> arguments) {
    Node callNode = NodeFactory.createCall(nodeType, functionName);
    for (Node arg : arguments) {
      callNode.appendChild(arg);
    }
    return callNode;
  }

  private static void testRenameVars(Compiler compiler, CompilerOptions options,
      String input, String expectedOutput) {
    CompilerPass pass = new RenameVars(compiler, "", false, false, false,
        null, null, null);

    CompilerInput inputCompiled = compiler.compile(Arrays.asList(input), options);
    assertNotNull(inputCompiled);

    Node externs = inputCompiled.getExterns();
    Node root = inputCompiled.getMain();

    // Run the pass
    pass.process(externs, root);

    String output = compiler.getSource();
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSimpleRename() {
    String input =
        "function foo(a, b) {" +
        "  var c = a * b;" +
        "  return c;";
    String expectedOutput =
        "function x(a, b) {" +
        "  var y = a * b;" +
        "  return y;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testSimpleRenameWithPrefix() {
    String input =
        "function foo(a, b) {" +
        "  var fa = a * b;" +
        "  return fa;";
    String expectedOutput =
        "function x(a, b) {" +
        "  var xa = a * b;" +
        "  return xa;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testVariableWithLongName() {
    String input =
        "function foo(a, b) {" +
        "  var longVariableNameThatShouldBeRenamed = a * b;" +
        "  return longVariableNameThatShouldBeRenamed;";
    String expectedOutput =
        "function x(a, b) {" +
        "  var xx = a * b;" +
        "  return xx;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testGlobalVariables() {
    String input =
        "var a = 1;" +
        "var b = 2;" +
        "function foo(x) {" +
        "  return x * a + b;" +
        "}";
    String expectedOutput =
        "var x = 1;" +
        "var y = 2;" +
        "function foo(x) {" +
        "  return x * x + y;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testLocalVariablesWithSameName() {
    String input =
        "function foo(a) {" +
        "  var a = 1;" +
        "  return a;" +
        "  var a = 2;" +
        "  return a;";
    String expectedOutput =
        "function foo(x) {" +
        "  var y = 1;" +
        "  return y;" +
        "  var z = 2;" +
        "  return z;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testLocalVariablesWithSameNameAndPrefix() {
    String input =
        "function foo(a) {" +
        "  var fa = 1;" +
        "  return fa;" +
        "  var fa = 2;" +
        "  return fa;";
    String expectedOutput =
        "function foo(x) {" +
        "  var fx = 1;" +
        "  return fx;" +
        "  var fy = 2;" +
        "  return fy;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testExternVariables() {
    String input =
        "var a = 1;" +
        "function foo(x) {" +
        "  return x * a;" +
        "}";
    String expectedOutput =
        "var x = 1;" +
        "function foo(x) {" +
        "  return x * x;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    String input =
        "function foo() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);";
    String expectedOutput =
        "function foo() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testPreserveAnonymousFunctionNamesWithLocalRenamingOnly() {
    String input =
        "function foo() {" +
        "  var x = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return x(2);";
    String expectedOutput =
        "function foo() {" +
        "  var x = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return x(2);";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testPreserveAnonymousFunctionNamesAndPrefix() {
    String input =
        "function foo() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);" +
        "}" +
        "function bar() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);";
    String expectedOutput =
        "function foo() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);" +
        "}" +
        "function bar() {" +
        "  var f = function(x) {" +
        "    return x * x;" +
        "  };" +
        "  return f(2);";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testGeneratePseudoNames() {
    String input =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a * b;" +
        "  return c;" +
        "}";
    String expectedOutput =
        "function foo() {" +
        "  var a__$0 = 1;" +
        "  var b__$1 = 2;" +
        "  var c__$2 = a__$0 * b__$1;" +
        "  return c__$2;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testGeneratePseudoNamesWithPrefix() {
    String input =
        "function foo() {" +
        "  var fa = 1;" +
        "  var fb = 2;" +
        "  var fc = fa * fb;" +
        "  return fc;" +
        "}";
    String expectedOutput =
        "function foo() {" +
        "  var fa__$0 = 1;" +
        "  var fb__$1 = 2;" +
        "  var fc__$2 = fa__$0 * fb__$1;" +
        "  return fc__$2;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithReservedNames() {
    String input =
        "function foo() {" +
        "  var if = 1;" +
        "  return if;" +
        "}";
    String expectedOutput =
        "function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "}";
    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test(expected = AssertionError.class)
  public void testRenameVarsWithSelfAssignment() {
    String input =
        "function foo() {" +
        "  var a = 1;" +
        "  a = a * 2;" +
        "  return a;";
    String expectedOutput =
        "function foo() {" +
        "  var a = 1;" +
        "  return a * 2;";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithInvalidNames() {
    String input =
        "function foo() {" +
        "  var 1 = 1;" +
        "  return 1;" +
        "}";
    String expectedOutput =
        "function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithFunctionsAsArguments() {
    String input =
        "function foo(a) {" +
        "  function bar(x) {" +
        "    return x * a(x);" +
        "  }" +
        "  return bar(2);" +
        "}";
    String expectedOutput =
        "function foo(a) {" +
        "  function bar(x) {" +
        "    return x * a(x);" +
        "  }" +
        "  return bar(2);" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithCycle() {
    String input =
        "function foo(a) {" +
        "  function bar(x) {" +
        "    var a = 1;" +
        "    return a + x(a);" +
        "  }" +
        "  return bar(2);" +
        "}" +
        "function x(a) {" +
        "  var a = 2;" +
        "  return a + foo(a);" +
        "}";
    String expectedOutput =
        "function foo(a) {" +
        "  function bar(x) {" +
        "    var y = 1;" +
        "    return y + x(y);" +
        "  }" +
        "  return bar(2);" +
        "}" +
        "function x(a) {" +
        "  var y = 2;" +
        "  return y + foo(y);" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithMultilineStatements() {
    String input =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a * b;" +
        "}" +
        "function bar() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a * b;" +
        "}";
    String expectedOutput =
        "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  return x * y;" +
        "}" +
        "function bar() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  return x * y;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithExportedVariables() {
    String input =
        "export var a = 1;" +
        "function foo() {" +
        "  return a;" +
        "}";
    String expectedOutput =
        "export var x = 1;" +
        "function foo() {" +
        "  return x;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }

  @Test
  public void testRenameVarsWithExportedVariablesAndLocalVariables() {
    String input =
        "export var a = 1;" +
        "function foo() {" +
        "  var b = 2;" +
        "  return b;" +
        "}";
    String expectedOutput =
        "export var x = 1;" +
        "function foo() {" +
        "  var y = 2;" +
        "  return y;" +
        "}";

    testRenameVars(new Compiler(), new CompilerOptions(), input, expectedOutput);
  }
}