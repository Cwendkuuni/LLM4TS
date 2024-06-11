
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

  // Test method: shouldTraverse(NodeTraversal t, Node n, Node parent)
  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.BLOCK)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.NAME)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.STRING_KEY)));
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT)));

    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.BLOCK)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.NAME)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.ASSIGN)));
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.STRING_KEY)));
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.SCRIPT)));
  }

  // Test method: visit(NodeTraversal t, Node n, Node parent)
  @Test
  public void testVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    NodeTraversal traversal = new NodeTraversal(checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    checkGlobalThis.visit(traversal, node, parent);
    // Expect error report
  }

  // Test method: shouldReportThis(Node n)
  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    // Test THIS on the left side of an assignment
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));

    // Test THIS with a property access
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.THIS));
    assertTrue(checkGlobalThis.shouldReportThis(parent));

    // Test other cases
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
    assertFalse(checkGlobalThis.shouldReportThis(null));
  }

  // Test method: getFunctionJsDocInfo(Node n)
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNull(jsDocInfo);

    node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo(true));
    node.addChildToFront(parent);
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    assertTrue(jsDocInfo.isConstructor());
  }
}
