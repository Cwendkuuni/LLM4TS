import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    this.compiler = new NoOpCompiler();
    this.functionToBlockMutator = new FunctionToBlockMutator(compiler, () -> "testId");
  }

  @Test
  public void testSimpleFunctionMutation() {
    Node functionNode = getFunctionNode("add", 2, new Node[] {Node.newInt(1), Node.newInt(3)});
    Node callNode = NodeUtil.newCallNode(Node.newString("add"), Node.newInt(5), functionNode, null);
    Node resultNode = functionToBlockMutator.mutate(
        "add", functionNode, callNode, "sum", false, false);

    Node expectedResult = NodeUtil.getFunctionBody(functionNode).cloneTree();
    expectedResult = NodeUtil.removeChildren(expectedResult, NodeUtil.getChildrenToKeep(expectedResult, NodeUtil.IS_VAR | NodeUtil.IS_ASSIGN));

    // Check that the function body has been mutated as expected.
    assertThat(resultNode).isEqualTo(expectedResult);
  }

  @Test
  public void testFunctionWithArgumentsMutation() {
    Node functionNode = getFunctionNode("multiply", 2, new Node[] {Node.newInt(2), Node.newInt(3)});
    Node callNode = NodeUtil.newCallNode(Node.newString("multiply"), Node.newInt(5), functionNode, null);
    Map<String, Node> arguments = new HashMap<>();
    arguments.put("a", NodeUtil.newInt(4));
    arguments.put("b", NodeUtil.newInt(6));
    Node resultNode = functionToBlockMutator.mutate(
        "multiply", functionNode, callNode, "product", true, false);

    // Check that the function body has been mutated as expected.
    Node expectedResult = NodeUtil.getFunctionBody(functionNode).cloneTree();
    expectedResult = NodeUtil.replaceChildren(expectedResult, arguments);
    expectedResult = NodeUtil.removeChildren(expectedResult, NodeUtil.getChildrenToKeep(expectedResult, NodeUtil.IS_VAR | NodeUtil.IS_ASSIGN));

    assertThat(resultNode).isEqualTo(expectedResult);
  }

  @Test
  public void testFunctionWithLabelMutation() {
    Node functionNode = getFunctionNode("labelFunction", 0, null);
    Node callNode = NodeUtil.newCallNode(Node.newString("labelFunction"), null, functionNode, null);
    String labelName = "foo";
    Node resultNode = functionToBlockMutator.mutate(
        "labelFunction", functionNode, callNode, null, false, true);

    Node expectedResult = NodeUtil.getFunctionBody(functionNode).cloneTree();
    expectedResult = NodeUtil.replaceChildren(expectedResult, Map.of("foo", NodeUtil.newLabelNode(labelName)));

    // Check that the function body has been mutated as expected.
    assertThat(resultNode).isEqualTo(expectedResult);
  }

  private Node getFunctionNode(String name, int numParams, Node[] params) {
    Node functionNode = NodeUtil.newFunctionNode(
        Node.newString(name), null, null, NodeUtil.newListNode(params), null, null);
    return functionNode;
  }
}