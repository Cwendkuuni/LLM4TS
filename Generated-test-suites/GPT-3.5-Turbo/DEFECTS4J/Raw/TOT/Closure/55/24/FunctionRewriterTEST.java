import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

  private FunctionRewriter rewriter;

  @Before
  public void setup() {
    rewriter = new FunctionRewriter();
  }

  @Test
  public void testProcess_EmptyFunctionReducer() {
    // Prepare
    Node externs = null;
    Node root = new Node(Token.ROOT);

    // Actions
    rewriter.process(externs, root);

    // Assertions
    // Check that the empty functions are replaced with the factory method call
    // TODO: Add assertions
  }

  @Test
  public void testProcess_IdentityReducer() {
    // Prepare
    Node externs = null;
    Node root = new Node(Token.ROOT);

    // Actions
    rewriter.process(externs, root);

    // Assertions
    // Check that the identity functions are replaced with the factory method call
    // TODO: Add assertions
  }

  @Test
  public void testProcess_ReturnConstantReducer() {
    // Prepare
    Node externs = null;
    Node root = new Node(Token.ROOT);

    // Actions
    rewriter.process(externs, root);

    // Assertions
    // Check that the functions returning constant literals are replaced with the factory method call
    // TODO: Add assertions
  }

  @Test
  public void testProcess_GetterReducer() {
    // Prepare
    Node externs = null;
    Node root = new Node(Token.ROOT);

    // Actions
    rewriter.process(externs, root);

    // Assertions
    // Check that the property getter methods are replaced with the factory method call
    // TODO: Add assertions
  }

  @Test
  public void testProcess_SetterReducer() {
    // Prepare
    Node externs = null;
    Node root = new Node(Token.ROOT);

    // Actions
    rewriter.process(externs, root);

    // Assertions
    // Check that the property setter methods are replaced with the factory method call
    // TODO: Add assertions
  }

}