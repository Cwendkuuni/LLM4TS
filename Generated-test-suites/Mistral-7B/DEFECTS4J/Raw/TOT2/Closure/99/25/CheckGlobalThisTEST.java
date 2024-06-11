import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.TokenType.*;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructorNullCompiler() {
    try {
      new CheckGlobalThis(null, CheckLevel.WARNING);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testCheckGlobalThisConstructorNullLevel() {
    try {
      new CheckGlobalThis(new AbstractCompiler(), null);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testShouldTraverseNullArguments() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = null;
    Node parentNode = null;
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseNullParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = null;
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseNonFunctionOrScriptParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = new Node();
    parentNode.setType(TOKEN);
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseNoAssignmentLeftHandSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = new Node();
    parentNode.setType(ASSIGN);
    parentNode.setFirstChild(node);
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode);
    assertFalse(result);
  }

  @Test(expected = NullPointerException.class)
  public void testVisitNullNode() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = null;
    Node parentNode = new Node();
    checkGlobalThis.visit(nodeTraversal, node, parentNode);
  }

  @Test(expected = NullPointerException.class)
  public void testVisitNullParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = null;
    checkGlobalThis.visit(nodeTraversal, node, parentNode);
  }

  @Test(expected = NullPointerException.class)
  public void testVisitNullNodeTraversal() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node();
    Node parentNode = new Node();
    checkGlobalThis.visit(null, node, parentNode);
  }

  @Test
  public void testVisitThisNotPresent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = new Node();
    node.setType(ASSIGN);
    parentNode.setType(TOKEN);
    checkGlobalThis.visit(nodeTraversal, node, parentNode);
  }

  @Test
  public void testVisitParentNotFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = new Node();
    parentNode.setType(TOKEN);
    node.setType(THIS);
    checkGlobalThis.visit(nodeTraversal, node, parentNode);
  }

  @Test
  public void testVisitParentNoJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node();
    Node parentNode = new Node();
    parentNode.setType(FUNCTION);
    node.setType(THIS);
    parentNode.setJSDocInfo(null);
    checkGlobalThis.visit(nodeTraversal, node, parentNode);
  }
}