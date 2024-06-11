import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledCode;
import com.google.javascript.rhino.CompiledSizeEstimator;
import com.google.javascript.rhino.CodeGenerator;
import com.google.javascript.rhino.CodePrinter;
import com.google.javascript.rhino.NodeOps;

import org.junit.Before;
import org.junit.Test;

public class InlineCostEstimatorTest {

  private Node rootNode;
  private CodePrinter printer;
  private NodeOps nodeOps;
  private CodeGenerator codeGenerator;
  private CompiledSizeEstimator sizeEstimator;

  @Before
  public void setUp() {
    printer = mock(CodePrinter.class);
    nodeOps = mock(NodeOps.class);
    codeGenerator = new CodeGenerator(printer, nodeOps);
    sizeEstimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  }

  @Test
  public void testGetCostOfEmptyNode() {
    Node emptyNode = NodeOps.NULL;
    InlineCostEstimator.getCost(emptyNode, sizeEstimator.getMaxCost());
    assertEquals(0, sizeEstimator.getCost());
  }

  @Test
  public void testGetCostOfSimpleNode() {
    rootNode = NodeOps.newIdent("x");
    when(nodeOps.getClassName(rootNode)).thenReturn("Identifier");

    InlineCostEstimator.getCost(rootNode, sizeEstimator.getMaxCost());
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, sizeEstimator.getCost());
  }

  @Test
  public void testGetCostOfComplexNode() {
    Node complexNode = NodeOps.newBinaryOp("+", NodeOps.number(1), NodeOps.number(2));
    when(nodeOps.getClassName(complexNode)).thenReturn("BinaryOp");

    InlineCostEstimator.getCost(complexNode, sizeEstimator.getMaxCost());
    assertEquals(4, sizeEstimator.getCost());
  }

  @Test
  public void testGetCostOfCodeGenerator() {
    String code = "function foo() {\n" +
                "  var x = 1;\n" +
                "  var y = 2;\n" +
                "  return x + y;\n" +
                "}";

    rootNode = Node.jsQuotedStringToNode(code);

    when(nodeOps.getClassName(rootNode)).thenReturn("Program");

    codeGenerator.add(rootNode);
    InlineCostEstimator.getCost(rootNode, sizeEstimator.getMaxCost());

    // Assuming each character is 1 byte, and each identifier is 2 chars
    int expectedCost = 37 + (3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST);
    assertEquals(expectedCost, sizeEstimator.getCost());
  }
}