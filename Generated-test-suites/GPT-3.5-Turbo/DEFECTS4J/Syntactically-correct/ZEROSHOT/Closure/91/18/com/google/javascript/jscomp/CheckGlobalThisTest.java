import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionConstructor() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();

    // Test
    boolean result = checkGlobalThis.shouldTraverse(t, new Node(Token.FUNCTION, new Node(Token.BLOCK)), new Node(Token.SCRIPT));

    // Verify
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().recordThis());

    // Test
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, new Node(Token.SCRIPT));

    // Verify
    assertFalse(result);
  }

  // More test methods to cover all cases...

}