import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleNode() {
    Node root = Node.IDENT("a");
    int cost = getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostOfComplexNode() {
    String complexCode = "function testFunction(a, b) {\n" +
            "  var c = a + b;\n" +
            "  if (c > 10) {\n" +
            "    var d = c * 2;\n" +
            "    return d;\n" +
            "  } else {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}";
    Node root = Node.parse(complexCode);
    int cost = getCost(root, 1000);
    int expectedCost = complexCode.getBytes().length;
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    int costThreshhold = -1;
    Node root = Node.IDENT("a");
    getCost(root, costThreshhold);
  }
}