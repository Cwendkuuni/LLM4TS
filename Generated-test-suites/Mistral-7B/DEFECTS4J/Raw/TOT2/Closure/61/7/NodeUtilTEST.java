import static org.junit.Assert.*; // For assertions
import static com.google.javascript.rhino.NodeType.*; // For Node types
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {
  // Test cases from Experts 1 and 2
  // ...

  // Test for isForIn method with invalid node
  @Test
  public void testIsForInInvalidNode() {
    Node invalidNode = new Node(IF, null);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  // Test for isStatement method with null node
  @Test(expected = NullPointerException.class)
  public void testIsStatementNullNode() {
    Node nullNode = null;
    NodeUtil.isStatement(nullNode);
  }

  // Test for isFunctionDeclaration method with null node
  @Test(expected = NullPointerException.class)
  public void testIsFunctionDeclarationNullNode() {
    Node nullNode = null;
    NodeUtil.isFunctionDeclaration(nullNode);
  }

  // ...
}