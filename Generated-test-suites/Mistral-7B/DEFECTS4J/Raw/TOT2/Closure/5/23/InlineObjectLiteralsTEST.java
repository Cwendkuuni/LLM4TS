import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineObjectLiterals;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: 1," +
        "  b: 2" +
        "};" +
        "var x = obj.a;" +
        "var y = obj.b;" +
        "}";

    InlineObjectLiterals pass = new InlineObjectLiterals(null, null);
    Node externs = NodePrinter.print(new IR().parse(sourceCode));
    Node root = new Node(Token.BLOCK);
    pass.process(externs, root);

    String expectedCode = "function foo() {" +
        "var a = 1, b = 2;" +
        "var x = a;" +
        "var y = b;" +
        "}";

    String actualCode = NodePrinter.print(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testInlineComplexObject() {
    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: {" +
        "    b: 1," +
        "    c: {d: 2, e: 3}" +
        "  }," +
        "  b: 2" +
        "};" +
        "var x = obj.a.b;" +
        "var y = obj.b;" +
        "}";

    InlineObjectLiterals pass = new InlineObjectLiterals(null, null);
    Node externs = NodePrinter.print(new IR().parse(sourceCode));
    Node root = new Node(Token.BLOCK);
    pass.process(externs, root);

    String expectedCode = "function foo() {" +
        "var a_b = 1, a_c_d = 2, a_c_e = 3," +
        "    a = {b: a_b, c: {d: a_c_d, e: a_c_e}}, " +
        "    b = 2;" +
        "var x = a_b;" +
        "var y = b;" +
        "}";

    String actualCode = NodePrinter.print(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testInlineObjectWithFunction() {
    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  bar: function() {return 1;}" +
        "};" +
        "var x = obj.bar();" +
        "}";

    InlineObjectLiterals pass = new InlineObjectLiterals(null, null);
    Node externs = NodePrinter.print(new IR().parse(sourceCode));
    Node root = new Node(Token.BLOCK);
    pass.process(externs, root);

    String expectedCode = "function foo() {" +
        "var bar = function() {return 1};" +
        "var x = bar();" +
        "}";

    String actualCode = NodePrinter.print(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testInlineObjectWithCycles() {
    String sourceCode = "function foo() {" +
        "var obj1 = {" +
        "  a: 1," +
        "  b: obj2" +
        "};" +
        "var obj2 = {" +
        "  c: 2," +
        "  a: obj1" +
        "};" +
        "}";

    InlineObjectLiterals pass = new InlineObjectLiterals(null, null);
    Node externs = NodePrinter.print(new IR().parse(sourceCode));
    Node root = new Node(Token.BLOCK);
    pass.process(externs, root);

    // The test will fail as InlineObjectLiterals doesn't support object cycles.
    // However, it's important to test for this edge case.
    String expectedCode = "function foo() {" +
        "var obj1 = {" +
        "  a: 1," +
        "  b: null" +
        "};" +
        "var obj2 = {" +
        "  c: 2," +
        "  a: null" +
        "};" +
        "obj1.b = obj2;" +
        "obj2.a = obj1;" +
        "}";

    String actualCode = NodePrinter.print(root);
    assertNotEquals(expectedCode, actualCode);
    fail("InlineObjectLiteralsTest: testInlineObjectWithCycles failed");
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  self: obj" +
        "};" +
        "}";

    InlineObjectLiterals pass = new InlineObjectLiterals(null, null);
    Node externs = NodePrinter.print(new IR().parse(sourceCode));
    Node root = new Node(Token.BLOCK);
    pass.process(externs, root);

    // The test will fail as InlineObjectLiterals doesn't support object self-references.
    // However, it's important to test for this edge case.
    String expectedCode = "function foo() {" +
        "var self = null;" +
        "var obj = {" +
        "  self: self" +
        "};" +
        "}";

    String actualCode = NodePrinter.print(root);
    assertNotEquals(expectedCode, actualCode);
    fail("InlineObjectLiteralsTest: testInlineObjectWithSelfReference failed");
  }
}