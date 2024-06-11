import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {
  private Compiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  /**
   * Test for empty function reduction.
   */
  @Test
  public void testEmptyFunctionReduction() {
    Node root = Node.newObject("Object");
    Node funcNode = Node.newFunction("func", null, null);
    root.addChild(funcNode);
    functionRewriter.process(null, root);
    Node expectedHelperCode = Node.newString("function emptyFn() {" +
            "  return function() {}" +
            "}");
    Node actualHelperCode = compiler.parseSyntheticCode("FunctionRewriterHelper",
            expectedHelperCode.toString());
    Node expectedFunctionCall = Node.newCall(
            Node.newString("FunctionRewriter.helper.emptyFn"), null);
    Node actualFunctionCall = funcNode;

    // Basic functionality test
    assertEquals("Expected empty function to be replaced with a helper call",
            expectedFunctionCall, actualFunctionCall);

    // Edge case: Empty function with arguments
    Node funcNodeWithArgs = Node.newFunction("func",
            Node.newArray(Node.newName("arg1"), Node.newName("arg2")), null);
    root.addChild(funcNodeWithArgs);
    functionRewriter.process(null, root);
    Node helperCodeWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.emptyFn"),
            Node.newArray(Node.newName("arg1"), Node.newName("arg2")));
    Node expectedFunctionCallWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.emptyFn"),
            Node.newArray(Node.newName("arg1"), Node.newName("arg2")));

    assertEquals("Expected empty function with arguments to be replaced with a " +
            "helper call with arguments", expectedFunctionCallWithArgs,
            actualFunctionCallWithArgs);

    // Exception handling: Function with a throw statement
    Node funcNodeWithThrow = Node.newFunction("func", null, null);
    funcNodeWithThrow.addChild(Node.newExpression(Node.THROW, null));
    root.addChild(funcNodeWithThrow);
    functionRewriter.process(null, root);

    // Check that the helper function was not applied since it doesn't save bytes
    assertNull("Expected empty function with a throw statement to remain unchanged",
            compiler.getNodeForCodeInsertion(null));
  }

  /**
   * Test for identity function reduction.
   */
  @Test
  public void testIdentityFunctionReduction() {
    Node root = Node.newObject("Object");
    Node funcNode = Node.newFunction("func", null, null);
    Node argNode = Node.newName("arg");
    funcNode.addChild(argNode);
    funcNode.addChild(Node.newReturn(argNode));
    root.addChild(funcNode);
    functionRewriter.process(null, root);
    Node expectedHelperCode = Node.newString("function identityFn(" +
            "identityFn_value) {" +
            "  return function(" + "identityFn_value) {" +
            "    return identityFn_value" +
            "  }" +
            "}");
    Node actualHelperCode = compiler.parseSyntheticCode("FunctionRewriterHelper",
            expectedHelperCode.toString());
    Node expectedFunctionCall = Node.newCall(
            Node.newString("FunctionRewriter.helper.identityFn"), argNode);

    // Basic functionality test
    assertEquals("Expected identity function to be replaced with a helper call",
            expectedFunctionCall, funcNode);

    // Edge case: Identity function with arguments
    Node funcNodeWithArgs = Node.newFunction("func",
            Node.newArray(Node.newName("arg1"), Node.newName("arg2")), null);
    Node arg1Node = Node.newName("arg1");
    Node arg2Node = Node.newName("arg2");
    funcNodeWithArgs.addChild(arg1Node);
    funcNodeWithArgs.addChild(arg2Node);
    funcNodeWithArgs.addChild(Node.newReturn(Node.newCall(
            Node.newName("identityFn"), arg1Node)));
    root.addChild(funcNodeWithArgs);
    functionRewriter.process(null, root);
    Node helperCodeWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.identityFn"), arg1Node);
    Node expectedFunctionCallWithArgs = Node.newCall(
            Node.newName("identityFn"), arg1Node);

    assertEquals("Expected identity function with arguments to be replaced with a " +
            "helper call with arguments", expectedFunctionCallWithArgs,
            funcNodeWithArgs);

    // Exception handling: Function with a throw statement
    Node funcNodeWithThrow = Node.newFunction("func", null, null);
    funcNodeWithThrow.addChild(Node.newExpression(Node.THROW, null));
    root.addChild(funcNodeWithThrow);
    functionRewriter.process(null, root);

    // Check that the helper function was not applied since it doesn't save bytes
    assertNull("Expected identity function with a throw statement to remain unchanged",
            compiler.getNodeForCodeInsertion(null));
  }

  /**
   * Test for getter function reduction.
   */
  @Test
  public void testGetterFunctionReduction() {
    Node root = Node.newObject("Object");
    Node propNameNode = Node.newString("propName");
    Node funcNode = Node.newFunction("func", null, null);
    funcNode.addChild(Node.newThis());
    funcNode.addChild(Node.newGetProp(Node.newThis(), propNameNode));
    funcNode.addChild(Node.newReturn(funcNode.getLastChild()));
    root.addChild(funcNode);
    functionRewriter.process(null, root);
    Node expectedHelperCode = Node.newString("function getter(" +
            "getter_name) {" +
            "  return function() {" +
            "this[" + getter_name + "]" +
            "}}");
    Node actualHelperCode = compiler.parseSyntheticCode("FunctionRewriterHelper",
            expectedHelperCode.toString());
    Node expectedFunctionCall = Node.newCall(
            Node.newString("FunctionRewriter.helper.getter"), propNameNode);

    // Basic functionality test
    assertEquals("Expected getter function to be replaced with a helper call",
            expectedFunctionCall, funcNode);

    // Edge case: Getter function with arguments
    Node funcNodeWithArgs = Node.newFunction("func",
            Node.newArray(Node.newName("arg1"), Node.newName("arg2")), null);
    funcNodeWithArgs.addChild(Node.newThis());
    funcNodeWithArgs.addChild(
            Node.newGetProp(Node.newThis(), Node.newString("propName")));
    funcNodeWithArgs.addChild(Node.newReturn(funcNodeWithArgs.getLastChild()));
    root.addChild(funcNodeWithArgs);
    functionRewriter.process(null, root);
    Node helperCodeWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.getter"),
            Node.newString("propName"));
    Node expectedFunctionCallWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.getter"),
            Node.newString("propName"));

    assertEquals("Expected getter function with arguments to be replaced with a " +
            "helper call with arguments", expectedFunctionCallWithArgs,
            funcNodeWithArgs);

    // Exception handling: Function with a throw statement
    Node funcNodeWithThrow = Node.newFunction("func", null, null);
    funcNodeWithThrow.addChild(Node.newExpression(Node.THROW, null));
    root.addChild(funcNodeWithThrow);
    functionRewriter.process(null, root);

    // Check that the helper function was not applied since it doesn't save bytes
    assertNull("Expected getter function with a throw statement to remain unchanged",
            compiler.getNodeForCodeInsertion(null));
  }

  /**
   * Test for setter function reduction.
   */
  @Test
  public void testSetterFunctionReduction() {
    Node root = Node.newObject("Object");
    Node propNameNode = Node.newString("propName");
    Node funcNode = Node.newFunction("func",
            Node.newArray(Node.newName("value")), null);
    Node thisNode = Node.newThis();
    Node setProp = Node.newExpression(Node.ASSIGN, null,
            Node.newGetProp(thisNode, propNameNode), Node.newName("value"));
    funcNode.addChild(setProp);
    funcNode.addChild(Node.newReturn(Node.newNull()));
    root.addChild(funcNode);
    functionRewriter.process(null, root);
    Node expectedHelperCode = Node.newString("function setter(" +
            "setter_name) {" +
            "  return function(" + "setter_value) {" +
            "this[" + setter_name + "] = setter_value" +
            "}}");
    Node actualHelperCode = compiler.parseSyntheticCode("FunctionRewriterHelper",
            expectedHelperCode.toString());
    Node expectedFunctionCall = Node.newCall(
            Node.newString("FunctionRewriter.helper.setter"), propNameNode);

    // Basic functionality test
    assertEquals("Expected setter function to be replaced with a helper call",
            expectedFunctionCall, funcNode);

    // Edge case: Setter function with arguments
    Node funcNodeWithArgs = Node.newFunction("func",
            Node.newArray(Node.newName("value"), Node.newName("arg1"),
                          Node.newName("arg2")), null);
    funcNodeWithArgs.addChild(Node.newThis());
    funcNodeWithArgs.addChild(Node.newGetProp(Node.newThis(), propNameNode));
    funcNodeWithArgs.addChild(Node.newExpression(Node.ASSIGN, null,
            funcNodeWithArgs.getLastChild(), Node.newName("value")));
    funcNodeWithArgs.addChild(Node.newReturn(Node.newNull()));
    root.addChild(funcNodeWithArgs);
    functionRewriter.process(null, root);
    Node helperCodeWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.setter"), propNameNode);
    Node expectedFunctionCallWithArgs = Node.newCall(
            Node.newString("FunctionRewriter.helper.setter"), propNameNode,
            Node.newName("value"));

    assertEquals("Expected setter function with arguments to be replaced with a " +
            "helper call with arguments", expectedFunctionCallWithArgs,
            funcNodeWithArgs);

    // Exception handling: Function with a throw statement
    Node funcNodeWithThrow = Node.newFunction("func", null, null);
    funcNodeWithThrow.addChild(Node.newExpression(Node.THROW, null));
    root.addChild(funcNodeWithThrow);
    functionRewriter.process(null, root);

    // Check that the helper function was not applied since it doesn't save bytes
    assertNull("Expected setter function with a throw statement to remain unchanged",
            compiler.getNodeForCodeInsertion(null));
  }
}