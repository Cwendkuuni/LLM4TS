import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    // Create test function node
    String functionName = "testFunction";
    Node functionNode = NodeUtil.newFunctionNode(null, functionName, null, null, null);

    // Create call node to test against
    String callName = "testCall";
    Node callNode = NodeUtil.newCallNode(null, null, null, null, NodeUtil.newString(Token.IDENTIFIER, callName));

    // Create mutator under test
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    // Test mutation
    Node mutatedNode = mutator.mutate(
        functionName, functionNode, callNode, "resultName", true, true);

    // Assert result is not null
    assertNotNull(mutatedNode);

    // Assert mutated node type is BLOCK
    assertEquals(Node.BLOCK, mutatedNode.getType());

    // Assert mutated node has same children as original function node
    Node origFunctionBody = NodeUtil.getFunctionBody(functionNode);
    Node mutatedFunctionBody = NodeUtil.getFunctionBody(mutatedNode);
    NodeUtil.assertTreeEquals(origFunctionBody, mutatedFunctionBody);

    // Assert labelNameForFunction was called correctly
    String labelName = mutator.getLabelNameForFunction(functionName);
    assertEquals("JSCompiler_inline_label_testFunction_" + LabelNameSupplier.ID_SUFFIX, labelName);
  }
}