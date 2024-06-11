import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: 2," +
      "    c: {d: 3}" +
      "  };" +
      "  return obj.b;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  var c_d = 3;" +
      "  return b;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsEmptyObject() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {};" +
      "  return obj;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var tempName_ = {};" +
      "  return tempName_;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsSelfReference() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: function() { return this.a; }" +
      "  };" +
      "  return obj.b();" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a = 1;" +
      "  var b = function() { return a; };" +
      "  return b();" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsCircularReference() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var objA = {" +
      "    a: 1," +
      "    b: objB" +
      "  };" +
      "  var objB = {" +
      "    b: objA" +
      "  };" +
      "  return objA.b;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);
  }

  @Test
  public void testInlineObjectLiteralsNestedProperties() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: {" +
      "      c: 2," +
      "      d: 3" +
      "    }" +
      "  };" +
      "  return obj.b.c;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a = 1;" +
      "  var b_c = 2;" +
      "  var b_d = 3;" +
      "  return b_c;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsNestedPropertiesWithValues() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: {" +
      "      c: {d: 2}," +
      "      e: function() { return this.c.d; }" +
      "    }" +
      "  };" +
      "  return obj.b.e();" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a = 1;" +
      "  var c_d = 2;" +
      "  var e = function() { return c_d; };" +
      "  return e();" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithVariables() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var x = 1;" +
      "  var obj = {" +
      "    a: x," +
      "    b: 2" +
      "  };" +
      "  return obj.a;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var x = 1;" +
      "  var a = x;" +
      "  return a;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithConditional() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: 2" +
      "  };" +
      "  if (true) {" +
      "    return obj.a;" +
      "  } else {" +
      "    return obj.b;" +
      "  }" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a = 1;" +
      "  return a;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithArray() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: [1, 2]" +
      "  };" +
      "  return obj.a[0];" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var a_0 = 1;" +
      "  return a_0;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithObjectProperties() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: {" +
      "      b: 1," +
      "      c: 2" +
      "    }" +
      "  };" +
      "  return obj.a.b;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var b = 1;" +
      "  return b;" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithObjectPropertiesAndValues() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var obj = {" +
      "    a: {" +
      "      b: 1," +
      "      c: function() { return this.b; }" +
      "    }" +
      "  };" +
      "  return obj.a.c();" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);
    pass.process(externs, root);

    String expected = "function foo() {" +
      "  var b = 1;" +
      "  var c = function() { return b; };" +
      "  return c();" +
      "}";

    String result = compiler.getSource(root);
    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicDependencies() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempName");

    String source = "function foo() {" +
      "  var objA = {" +
      "    a: 1," +
      "    b: objB" +
      "  };" +
      "  var objB = {" +
      "    b: objA" +
      "  };" +
      "  return objA.b;" +
      "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseAndCompile(externs, root, source);

    try {
      pass.process(externs, root);
      fail("Should have thrown an error.");
    } catch (AssertionError e) {
      // Expected
    }
  }
}