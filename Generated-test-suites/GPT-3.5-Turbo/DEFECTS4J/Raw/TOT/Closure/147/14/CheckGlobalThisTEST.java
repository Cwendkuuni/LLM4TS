import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void shouldNotReportSafeThisUsage() {
    NodeTraversal t = createNodeTraversal();
    Node n = createFunctionNode();
    Node parent = createParentNode();

    checkGlobalThis.visit(t, n, parent);

    // No error should be reported
    assertFalse(compiler.getErrorCount() > 0);
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignment() {
    NodeTraversal t = createNodeTraversal();
    Node n = createThisNode();
    Node parent = createParentNode();

    checkGlobalThis.visit(t, n, parent);

    // An error about using global 'this' object should be reported
    assertTrue(compiler.getErrorCount() > 0);
    assertEquals(1, compiler.getErrorCount());
    assertEquals("dangerous use of the global 'this' object", compiler.getErrors()[0].getDescription());
  }

  @Test
  public void shouldReportThisWithPropertyAccess() {
    NodeTraversal t = createNodeTraversal();
    Node n = createThisNode();

    Node getterNode = createGetterNode();
    Node parent = createParentNode(getterNode);

    checkGlobalThis.visit(t, n, parent);

    // An error about using global 'this' object should be reported
    assertTrue(compiler.getErrorCount() > 0);
    assertEquals(1, compiler.getErrorCount());
    assertEquals("dangerous use of the global 'this' object", compiler.getErrors()[0].getDescription());
  }

  private NodeTraversal createNodeTraversal() {
    return new NodeTraversal();
  }

  private Node createFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }

  private Node createParentNode() {
    Node parent = new Node(Token.BLOCK);
    return parent;
  }

  private Node createThisNode() {
    Node thisNode = new Node(Token.THIS);
    return thisNode;
  }

  private Node createParentNode(Node child) {
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(child);
    return parent;
  }

  private Node createGetterNode() {
    Node getterNode = new Node(Token.NAME, "getter");
    return getterNode;
  }
}