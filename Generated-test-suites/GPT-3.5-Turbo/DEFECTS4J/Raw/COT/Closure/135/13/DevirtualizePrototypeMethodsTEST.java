
import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  
  @Test
  public void testRewriteDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods(null);
    // Create test node
    Node node = Node.newString(Token.NAME, "test");
    Node parent = Node.newString(Token.NAME, "parent");
    parent.addChildToBack(node);
    Node expr = Node.newString(Token.NAME, "expr");
    expr.addChildToBack(parent);
    Node block = Node.newString(Token.NAME, "block");
    block.addChildToBack(expr);
    Node functionNode = Node.newString(Token.NAME, "function");
    functionNode.addChildToBack(Node.newString(Token.PARAM_LIST, ""));
    block.addChildAfter(functionNode, expr);

    dpm.rewriteDefinition(node, "newMethodName");

    assertEquals("var newMethodName = function(self) {self; };", block.toString());
  }

  @Test
  public void testRewriteCallSites() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods(null);
    // Create test node
    Node node = Node.newString(Token.NAME, "test");
    Node parent = Node.newString(Token.NAME, "parent");
    parent.addChildToBack(node);
    Node expr = Node.newString(Token.NAME, "expr");
    expr.addChildToBack(parent);

    dpm.rewriteCallSites(null, null, "newMethodName");

    assertEquals("newMethodName(null);", expr.toString());
  }

}
