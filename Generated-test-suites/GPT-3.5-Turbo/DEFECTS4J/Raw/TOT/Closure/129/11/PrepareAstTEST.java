import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  private static PrepareAst prepareAst;

  @BeforeClass
  public static void setUp() {
    prepareAst = new PrepareAst();
  }

  @Test
  public void testNormalizeNodeTypes() {
    // Test typical use case
    Node root = new Node(Token.ROOT);
    Node child1 = new Node(Token.EXPR_VOID);
    Node child2 = new Node(Token.EXPR_RESULT);

    root.addChildToFront(child1);
    root.addChildToBack(child2);

    prepareAst.normalizeNodeTypes(root);

    assertTrue("Child1 should have been converted to EXPR_RESULT",
               child1.isExprResult());
    assertTrue("Child2 should remain as EXPR_RESULT",
               child2.isExprResult());

    // Test edge case with no child
    Node emptyRoot = new Node(Token.ROOT);

    prepareAst.normalizeNodeTypes(emptyRoot);

    assertNull("Empty root should have no child", emptyRoot.getFirstChild());
  }

  @Test
  public void testNormalizeBlocks() {
    // Test typical use case
    Node ifNode = new Node(Token.IF);
    Node child1 = new Node(Token.BLOCK);
    Node child2 = new Node(Token.LEFT);
    ifNode.addChildToFront(child1);
    ifNode.addChildToBack(child2);

    prepareAst.normalizeBlocks(ifNode);

    assertTrue("Child2 should be added to the new block",
               child1.getFirstChild() == child2);

    // Test edge case where there are no child nodes in ifNode
    Node emptyIf = new Node(Token.IF);

    prepareAst.normalizeBlocks(emptyIf);

    assertNull("Empty if node should remain empty", emptyIf.getFirstChild());
  }

  @Test
  public void testPrepareAnnotations() {
    // Test shouldTraverse() method with object literal
    Node objLit = new Node(Token.OBJECTLIT);
    prepareAst.new PrepareAnnotations().shouldTraverse(null, objLit, null);

    // Test annotateCalls() method
    Node callNode = new Node(Token.CALL);
    Node firstChild = new Node(Token.NAME);

    callNode.addChildToFront(firstChild);
    prepareAst.new PrepareAnnotations().annotateCalls(callNode);

    assertTrue("CallNode should have the FREE_CALL property",
               callNode.getBooleanProp(Node.FREE_CALL));

    Node evalNode = new Node(Token.NAME);
    evalNode.setString("eval");

    callNode.addChildToFront(evalNode);
    prepareAst.new PrepareAnnotations().annotateCalls(callNode);

    assertTrue("EvalNode should have the DIRECT_EVAL property",
               evalNode.getBooleanProp(Node.DIRECT_EVAL));

    // Test annotateDispatchers() method
    Node assignNode = new Node(Token.ASSIGN);
    Node functionNode = new Node(Token.FUNCTION);
    assignNode.addChildToBack(functionNode);

    prepareAst.new PrepareAnnotations().annotateDispatchers(functionNode, assignNode);

    assertTrue("FunctionNode should have the IS_DISPATCHER property",
               functionNode.getBooleanProp(Node.IS_DISPATCHER));

    // Test normalizeObjectLiteralKeyAnnotations() method
    Node objLiteral = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING);
    Node value = new Node(Token.FUNCTION);
    key.addChildToBack(new Node(Token.NAME, "a"));

    objLiteral.addChildToBack(key);
    key.addChildToBack(value);

    prepareAst.new PrepareAnnotations().normalizeObjectLiteralKeyAnnotations(objLiteral, key, value);

    assertNull("Key node should not have JSDocInfo after normalization",
               key.getJSDocInfo());
    assertEquals("Value node should have the JSDocInfo from key",
               key.getJSDocInfo(), value.getJSDocInfo());
  }
}