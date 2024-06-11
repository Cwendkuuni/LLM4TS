import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = () -> "test_id";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newNullNode());
    String resultName = "result";
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node body = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(0, body.getChildCount());
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newArray(
        NodeUtil.newStringNode("arg1"), NodeUtil.newNumberNode(1)
    ));
    String resultName = "result";
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node body = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(2, body.getChildCount());
    Node paramAssign1 = body.getChild(0);
    Node paramAssign2 = body.getChild(1);

    assertEquals("arg1", paramAssign1.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNumberNode(1), paramAssign1.getLastChild());
    assertEquals("arg2", paramAssign2.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), paramAssign2.getLastChild());
  }

  @Test
  public void testMutateFunctionWithLabel() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newNullNode());
    String resultName = "result";
    String labelName = "test_label";
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node body = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(3, body.getChildCount());
    Node labelNode = body.getChild(0);
    Node labelNameNode = labelNode.getFirstChild();

    assertEquals("test_label", labelNameNode.getStringValue());
    Node bodyNode = body.getChild(1);
    assertEquals(NodeUtil.newBlock(NodeUtil.newNullNode()), bodyNode);
  }

  @Test
  public void testMutateFunctionWithLabelAndArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newArray(
        NodeUtil.newStringNode("arg1"), NodeUtil.newNumberNode(1)
    ));
    String resultName = "result";
    String labelName = "test_label";
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node body = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(4, body.getChildCount());
    Node labelNode = body.getChild(0);
    Node labelNameNode = labelNode.getFirstChild();

    assertEquals("test_label", labelNameNode.getStringValue());
    Node paramAssign1 = body.getChild(1);
    Node paramAssign2 = body.getChild(2);

    assertEquals("arg1", paramAssign1.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNumberNode(1), paramAssign1.getLastChild());
    assertEquals("arg2", paramAssign2.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), paramAssign2.getLastChild());
  }

  @Test
  public void testMutateFunctionWithLabelAndArgumentsAndResult() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newArray(
        NodeUtil.newStringNode("arg1"), NodeUtil.newNumberNode(1)
    ));
    String resultName = "result";
    String labelName = "test_label";
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, true, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node body = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(5, body.getChildCount());
    Node labelNode = body.getChild(0);
    Node labelNameNode = labelNode.getFirstChild();

    assertEquals("test_label", labelNameNode.getStringValue());
    Node paramAssign1 = body.getChild(1);
    Node paramAssign2 = body.getChild(2);
    Node resultAssign = body.getChild(3);

    assertEquals("arg1", paramAssign1.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNumberNode(1), paramAssign1.getLastChild());
    assertEquals("arg2", paramAssign2.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), paramAssign2.getLastChild());
    assertEquals(resultName, resultAssign.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), resultAssign.getLastChild());
  }

  @Test
  public void testMutateFunctionWithLabelAndArgumentsAndResultAndBody() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode("testFunction", null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", NodeUtil.newArray(
        NodeUtil.newStringNode("arg1"), NodeUtil.newNumberNode(1)
    ));
    String resultName = "result";
    String labelName = "test_label";
    Node body = NodeUtil.newBlock(NodeUtil.newNumberNode(42));
    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, true, false);

    assertEquals("testFunction", newFnNode.getClass().getName());
    Node newBody = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(6, newBody.getChildCount());
    Node labelNode = newBody.getChild(0);
    Node labelNameNode = labelNode.getFirstChild();

    assertEquals("test_label", labelNameNode.getStringValue());
    Node paramAssign1 = newBody.getChild(1);
    Node paramAssign2 = newBody.getChild(2);
    Node resultAssign = newBody.getChild(3);
    Node newBodyNode = newBody.getChild(4);

    assertEquals("arg1", paramAssign1.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNumberNode(1), paramAssign1.getLastChild());
    assertEquals("arg2", paramAssign2.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), paramAssign2.getLastChild());
    assertEquals(resultName, resultAssign.getFirstChild().getStringValue());
    assertEquals(NodeUtil.newNullNode(), resultAssign.getLastChild());
    assertEquals(body, newBodyNode);
  }
}