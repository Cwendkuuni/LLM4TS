import org.junit.Test;
import static com.google.common.truth.Truth.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.NodeOperations;

public class PrepareAstTest {

  @Test
  public void testPrepareAstWithSimpleExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("1 + 2");
    prepareAst.process(null, root);

    // Assert that the root node type is EXPR_CONST and its value is 3
    assertThat(root.getType()).isEqualTo(Token.EXPR_CONST);
    assertThat(root.getInt()).isEqualTo(3);
  }

  @Test
  public void testPrepareAstWithControlStructures() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("if (true) {}",
        "while (true) {}",
        "for (var i = 0; i < 10; i++) {}",
        "do {} while (false);");
    prepareAst.process(null, root);

    // Assert that all control structures have a block as their child
    for (Node controlStructure = root.getFirstChild();
         controlStructure != null; controlStructure = controlStructure.getNext()) {
      assertTrue(controlStructure.isControlStructure());
      Node block = controlStructure.getFirstChild();
      assertTrue(block.isBlock());
    }
  }

  @Test
  public void testPrepareAstWithCalls() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("function foo() {" +
        "return Math.sqrt(2);" +
        "}" +
        "Math.sqrt(2);");
    prepareAst.process(null, root);

    // Assert that Math.sqrt call is marked as a free call
    Node mathSqrtCall = root.getLastChild();
    assertTrue(mathSqrtCall.isCall());
    assertTrue(mathSqrtCall.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAstWithDirectEval() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("(0, eval)('1 + 2')");
    prepareAst.process(null, root);

    // Assert that the direct eval call is marked as a direct eval
    Node evalCall = root.getFirstChild();
    assertTrue(evalCall.isCall());
    assertTrue(evalCall.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAstWithObjectLiteral() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("{" +
        "a: 1," +
        "b: function() { return 2; }" +
        "}");
    prepareAst.process(null, root);

    // Assert that the object literal has the expected properties
    Node objectLit = root;
    Node a = objectLit.getFirstChild();
    Node b = a.getNext();
    assertTrue(a.isName());
    assertTrue(a.getString().equals("a"));
    assertTrue(a.isInt());
    assertTrue(a.getInt() == 1);

    assertTrue(b.isObjectLit());
    Node function = b.getFirstChild();
    assertTrue(function.isFunction());
  }

  @Test
  public void testPrepareAstWithObjectLiteralAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("{" +
        "/** @type {number} */\n" +
        "a: 1," +
        "/** @type {Function}\n" +
        " * @return {number}\n" +
        " */\n" +
        "b: function() { return 2; }" +
        "}");
    prepareAst.process(null, root);

    // Assert that the function literal inherits the JSDoc annotations
    Node objectLit = root;
    Node a = objectLit.getFirstChild();
    Node b = a.getNext();
    Node function = b.getFirstChild();
    assertEquals("Function", function.getJSDocInfo().getType());
    assertEquals("@type Function\n* @return number", function.getJSDocInfo().getCommentText());
  }

  @Test
  public void testPrepareAstWithMixedObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = NodeOperations.parseJsonString("{" +
        "a: 1," +
        "/** @type {number} */\n" +
        "b: function() { return 2; }" +
        "c: 3" +
        "}");
    prepareAst.process(null, root);

    // Assert that the object literal has the expected properties
    Node objectLit = root;
    Node a = objectLit.getFirstChild();
    Node b = a.getNext();
    Node function = b.getFirstChild();
    Node c = b.getNext();
    assertTrue(a.isName());
    assertTrue(a.getString().equals("a"));
    assertTrue(a.isInt());
    assertTrue(a.getInt() == 1);

    assertTrue(function.isFunction());
    assertEquals("Function", function.getJSDocInfo().getType());
    assertEquals("@type Function\n* @return number", function.getJSDocInfo().getCommentText());

    assertTrue(c.isName());
    assertTrue(c.getString().equals("c"));
    assertTrue(c.isInt());
    assertTrue(c.getInt() == 3);
  }
}