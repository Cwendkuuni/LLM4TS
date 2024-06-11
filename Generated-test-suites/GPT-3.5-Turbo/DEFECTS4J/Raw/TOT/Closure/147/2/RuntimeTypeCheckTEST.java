import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  private RuntimeTypeCheck runtimeTypeCheck;
  private MockCompiler compiler;

  @Before
  public void setUp() {
    compiler = new MockCompiler();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testAddMarkers() {
    // Create a test class node
    Node classNode = createClassNode("Test");
    
    // Invoke the visit method
    runtimeTypeCheck.addMarkers(classNode);

    // Verify that the marker properties are added correctly
    assertEquals(1, classNode.getChildCount()); // Check the number of children
    Node childNode = classNode.getFirstChild();
    assertEquals(Token.EXPR_RESULT, childNode.getType()); // Check the type of the child node
    assertEquals(Token.ASSIGN, childNode.getFirstChild().getType()); // Check the type of the first child of the child node
    assertEquals(Token.GETELEM, childNode.getFirstChild().getFirstChild().getType()); // Check the type of the first grandchild of the child node
    assertEquals(Token.GETPROP, childNode.getFirstChild().getFirstChild().getFirstChild().getType()); // Check the type of the first great-grandchild of the child node
    assertEquals(Token.NAME, childNode.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getType()); // Check the type of the first great-great-grandchild of the child node
    assertEquals("C.prototype", childNode.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getString()); // Check the name of the first great-great-grandchild of the child node
    assertEquals(Token.NEWSTRING, childNode.getFirstChild().getFirstChild().getFirstChild().getNext().getType()); // Check the type of the second great-great-grandchild of the child node
    assertEquals("instance_of__C", childNode.getFirstChild().getFirstChild().getFirstChild().getNext().getString()); // Check the name of the second great-great-grandchild of the child node
    assertEquals(Token.TRUE, childNode.getFirstChild().getNext().getType()); // Check the type of the second child of the child node
  }

  @Test
  public void testAddChecks() {
    // Create a test function node
    Node functionNode = createFunctionNode("testFunction");

    // Invoke the visit method
    runtimeTypeCheck.addChecks(functionNode);

    // Verify that the check calls are added correctly
    assertEquals(1, functionNode.getLastChild().getChildCount()); // Check the number of children of the last child node
    Node checkNode = functionNode.getLastChild().getFirstChild();
    assertEquals(Token.CALL, checkNode.getType()); // Check the type of the check call
    assertEquals(runtimeTypeCheck.jsCode("checkType"), checkNode.getFirstChild()); // Check the function name
    assertEquals(Token.NAME, checkNode.getSecondChild().getType()); // Check the type of the second child of the check call
    assertEquals("x", checkNode.getSecondChild().getString()); // Check the name of the second child of the check call
    assertEquals(Token.ARRAYLIT, checkNode.getLastChild().getType()); // Check the type of the last child of the check call

    // Verify that the checker nodes are added correctly
    assertEquals(1, checkNode.getLastChild().getChildCount()); // Check the number of children of the last child of the check call
    Node checkerNode = checkNode.getLastChild().getFirstChild();
    assertEquals(Token.CALL, checkerNode.getType()); // Check the type of the checker call
    assertEquals(runtimeTypeCheck.jsCode("valueChecker"), checkerNode.getFirstChild()); // Check the function name
    assertEquals("string", checkerNode.getSecondChild().getString()); // Check the argument of the checker call
  }

  private Node createClassNode(String className) {
    Node classNode = new Node(Token.CLASS);
    Node nameNode = new Node(Token.NAME, className);
    classNode.addChildToBack(nameNode);
    return classNode;
  }

  private Node createFunctionNode(String functionName) {
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, functionName);
    functionNode.addChildToBack(nameNode);
    Node blockNode = new Node(Token.BLOCK);
    functionNode.addChildToBack(blockNode);
    return functionNode;
  }

  private class MockCompiler extends AbstractCompiler {

    @Override
    public CompilerContext getCompilerContext() {
      return null;
    }

    @Override
    public CodingConvention getCodingConvention() {
      return null;
    }

    @Override
    public boolean hasTypeCheckingRun() {
      return false;
    }

    @Override
    public void reportChangeToEnclosingScope(Node n) {}

    @Override
    public Node getNodeForCodeInsertion(Node n) {
      return null;
    }

    @Override
    public void reportCodeChange() {}
  }
}