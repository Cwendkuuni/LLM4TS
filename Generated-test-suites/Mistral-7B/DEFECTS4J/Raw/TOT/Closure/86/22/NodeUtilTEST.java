import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1, null, null, null, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newCall(1, "function() {}");
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newCall(1, "console.log(1)"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newCall(1, "function() {}");
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "myFunction", new String[]{"param"}, Node.SCOPE_GLOBAL, Node.newBlock(1, new Node[]{}));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newCall(1, "function() {}");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, "myFunction", new String[]{"param"}, Node.SCOPE_GLOBAL, Node.newBlock(1, new Node[]{}));
    Node parentScript = Node.newBlock(1, new Node[] {nodeHoistedFunctionDeclaration});
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInsideFunction = Node.newFunction(1, "myFunction", new String[]{"param"}, Node.SCOPE_FUNCTION, Node.newBlock(1, new Node[] {}));
    Node parentFunction = Node.newFunction(1, "outerFunction", new String[]{}, Node.SCOPE_GLOBAL, Node.newBlock(1, new Node[] {nodeFunctionDeclarationInsideFunction}));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInsideFunction));
  }
}