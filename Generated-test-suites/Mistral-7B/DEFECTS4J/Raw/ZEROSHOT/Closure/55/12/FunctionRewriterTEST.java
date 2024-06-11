import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Token;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;

public class FunctionRewriterTest {
  private Compiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    String source = "function Test() {}\n" +
        "Test.prototype.testMethod = function() {};";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testMethodNode = findNode("Test.prototype.testMethod", root);
    assertNotNull(testMethodNode);
    assertEquals(Token.CALL, testMethodNode.getType());
    assertEquals("JSCompiler_emptyFn", testMethodNode.getFirstChild().getString());
  }

  @Test
  public void testIdentityReducer() {
    String source = "function Test() {}\n" +
        "Test.prototype.testMethod = function(a) {return a}";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testMethodNode = findNode("Test.prototype.testMethod", root);
    assertNotNull(testMethodNode);
    assertEquals(Token.CALL, testMethodNode.getType());
    assertEquals("JSCompiler_identityFn", testMethodNode.getFirstChild().getString());
  }

  @Test
  public void testGetterReducer() {
    String source = "function Test() {}\n" +
        "Test.prototype.testProperty = function() {return this.testProperty_}";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testPropertyNode = findNode("Test.prototype.testProperty", root);
    assertNotNull(testPropertyNode);
    assertEquals(Token.CALL, testPropertyNode.getType());
    assertEquals("JSCompiler_get", testPropertyNode.getFirstChild().getString());
    assertEquals("testProperty_", testPropertyNode.getFirstChild().getLastChild().getString());
  }

  @Test
  public void testSetterReducer() {
    String source = "function Test() {}\n" +
        "Test.prototype.testProperty = function(value) {this.testProperty_ = value}";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testPropertyNode = findNode("Test.prototype.testProperty", root);
    assertNotNull(testPropertyNode);
    assertEquals(Token.CALL, testPropertyNode.getType());
    assertEquals("JSCompiler_set", testPropertyNode.getFirstChild().getString());
    assertEquals("testProperty_", testPropertyNode.getFirstChild().getLastChild().getString());
  }

  @Test
  public void testReturnConstantReducer() {
    String source = "function Test() {}\n" +
        "Test.prototype.testMethod = function() {return 10}";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testMethodNode = findNode("Test.prototype.testMethod", root);
    assertNotNull(testMethodNode);
    assertEquals(Token.CALL, testMethodNode.getType());
    assertEquals("JSCompiler_returnArg", testMethodNode.getFirstChild().getString());
    assertEquals(10, testMethodNode.getLastChild().getNumber());
  }

  private Node findNode(String nodeName, Node root) {
    NodeTraversal traversal = new NodeTraversal(root) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return node.getString().equals(nodeName);
      }
    };
    traversal.traverse();
    return traversal.getCurrentNode();
  }

  @Test
  public void testNoReduction() {
    String source = "function Test() {}\n" +
        "Test.prototype.testMethod = function() {return 1 + 1}";

    Node root = Node.parse(source);
    functionRewriter.process(null, root);

    Node testMethodNode = findNode("Test.prototype.testMethod", root);
    assertNotNull(testMethodNode);
    assertNotEquals(Token.CALL, testMethodNode.getType());
  }
}