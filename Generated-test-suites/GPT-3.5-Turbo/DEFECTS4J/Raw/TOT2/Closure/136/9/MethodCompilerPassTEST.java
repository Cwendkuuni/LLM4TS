import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testExternMethods() {
    // Test if externMethods is populated correctly
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    assertEquals(0, pass.externMethods.size());
    pass.process(null, null);
    assertTrue(pass.externMethods.contains("exampleMethod1"));
  }

  @Test
  public void testNonMethodProperties() {
    // Test if nonMethodProperties is populated correctly
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    assertEquals(0, pass.nonMethodProperties.size());
    pass.process(null, null);
    assertTrue(pass.nonMethodProperties.contains("exampleProperty"));
  }

  // Additional test cases for all public methods in MethodCompilerPass

  @Test
  public void testAddPossibleSignatureFunction() {
    // Test adding a function signature
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    pass.addPossibleSignature("exampleFunction", new Node(Token.FUNCTION), new NodeTraversal());
    assertTrue(pass.methodDefinitions.containsKey("exampleFunction"));
  }

  @Test
  public void testAddPossibleSignatureName() {
    // Test adding a name signature
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    pass.addPossibleSignature("exampleFunction", new Node(Token.NAME), new NodeTraversal());
    assertTrue(pass.nonMethodProperties.contains("exampleFunction"));
  }

  @Test
  public void testProcessNodeTraversalGetProp() {
    // Test processing a GETPROP node
    Node getPropNode = new Node(Token.GETPROP, new Node(Token.NAME, "example"), new Node(Token.STRING, "method"));
    Node root = new Node(Token.SCRIPT, getPropNode);
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    pass.process(null, root);
    assertTrue(pass.methodDefinitions.containsKey("example"));
  }

  @Test
  public void testProcessNodeTraversalObjectLit() {
    // Test processing an OBJECTLIT node
    Node objectLitNode = new Node(Token.OBJECTLIT, new Node(Token.STRING, "example"), new Node(Token.FUNCTION));
    Node root = new Node(Token.SCRIPT, objectLitNode);
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    pass.process(null, root);
    assertTrue(pass.methodDefinitions.containsKey("example"));
  }
}