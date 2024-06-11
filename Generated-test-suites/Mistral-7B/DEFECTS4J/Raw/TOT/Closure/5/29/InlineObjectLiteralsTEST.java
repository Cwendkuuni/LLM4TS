import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c();" +
        "  return [x, y, z];" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    String expectedCode = "function f() {" +
        "  var a = temporaryVar_a_0," +
        "      b = temporaryVar_b_1," +
        "      c = function() { return temporaryVar_c_2; }:" +
        "  return [a, b, c()];" +
        "}" +
        "var temporaryVar_a_0 = 1," +
        "    temporaryVar_b_1 = 2," +
        "    temporaryVar_c_2 = function() { return 3; };";

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithDeletedProperty() {
    String jsCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "  };" +
        "  delete obj.b;" +
        "  return obj.a;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    String expectedCode = "function f() {" +
        "  var a = temporaryVar_a_0;" +
        "  return a;" +
        "}" +
        "var temporaryVar_a_0 = 1;";

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String jsCode = "function f() {" +
        "  var obj = {" +
        "    a: function() { return obj.b; }" +
        "  };" +
        "  obj.b = f;" +
        "  return obj.a();" +
        "}" +
        "return f();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    // The code becomes invalid due to the self-reference
    // The test case here is to verify that the InlineObjectLiterals pass
    // correctly identifies and skips the self-referential object literal.
    fail("InlineObjectLiterals pass failed to handle self-referential object literal.");
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionExpression() {
    String jsCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() { return 2; }" +
        "  };" +
        "  return obj.b();" +
        "}" +
        "return f();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    String expectedCode = "function f() {" +
        "  var a = temporaryVar_a_0," +
        "      b = function() { return temporaryVar_b_1; }:" +
        "  return b();" +
        "}" +
        "var temporaryVar_a_0 = 1," +
        "    temporaryVar_b_1 = 2;";

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    String jsCode = "function f() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: 2" +
        "    }," +
        "    d: 3" +
        "  };" +
        "  return obj.a.b;" +
        "}" +
        "return f();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    String expectedCode = "function f() {" +
        "  var a_b = temporaryVar_a_b_0," +
        "      a_c = temporaryVar_a_c_1;" +
        "  return a_b;" +
        "}" +
        "var temporaryVar_a_b_0 = 1," +
        "    temporaryVar_a_c_1 = 2;" +
        "var temporaryVar_a_0 = {" +
        "  a: {" +
        "    b: temporaryVar_a_b_0," +
        "    c: temporaryVar_a_c_1" +
        "  }, " +
        "  d: 3" +
        "};";

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    String jsCode = "function f() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: obj2" +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1" +
        "  };" +
        "  return obj1.a;" +
        "}" +
        "return f();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    // The code becomes invalid due to the cyclic object literal dependency
    // The test case here is to verify that the InlineObjectLiterals pass
    // correctly identifies and skips the cyclic object literal.
    fail("InlineObjectLiterals pass failed to handle cyclic object literal.");
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String jsCode = "var MY_VAR = {" +
        "  a: 1," +
        "  b: 2" +
        "};" +
        "function f() {" +
        "  return MY_VAR.a;" +
        "}" +
        "return f();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temporaryVar");
    Node root = NodePrinter.print(jsCode);
    pass.process(null, root);
    String result = NodePrinter.printToString(compiler.getProgramText());

    // The InlineObjectLiterals pass should not inline the exported variable
    String expectedCode = "var MY_VAR = {" +
        "  a: 1," +
        "  b: 2" +
        "};" +
        "function f() {" +
        "  return MY_VAR.a;" +
        "}" +
        "return f();";

    assertEquals(expectedCode, result);
  }
}