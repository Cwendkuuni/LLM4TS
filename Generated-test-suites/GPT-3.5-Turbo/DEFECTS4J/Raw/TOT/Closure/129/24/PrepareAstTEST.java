import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    // Typical use case: normalizeNodeTypes should replace all EXPR_VOID nodes with EXPR_RESULT nodes
    PrepareAst ast = new PrepareAst();
    Node root = new Node(Token.ROOT);
    Node exprVoid = new Node(Token.EXPR_VOID);
    Node exprResult = new Node(Token.EXPR_RESULT);
    root.addChildToBack(exprVoid);
    ast.normalizeNodeTypes(root);
    assertEquals(exprResult, root.getFirstChild());
    
    // Edge case: normalizeNodeTypes should handle empty nodes correctly
    Node emptyNode = new Node(Token.NAME);
    root.addChildToBack(emptyNode);
    ast.normalizeNodeTypes(root);
    assertEquals(exprResult, root.getFirstChild());
    assertEquals(emptyNode, root.getLastChild());
    
  }

  @Test
  public void testNormalizeBlocks() {
    // Typical use case: normalizeBlocks should add a block to an IF node
    PrepareAst ast = new PrepareAst();
    Node ifNode = new Node(Token.IF);
    Node statement = new Node(Token.IF);
    ifNode.addChildToBack(statement);
    ast.normalizeBlocks(ifNode);
    assertTrue(ifNode.getFirstChild().isBlock());
    
    // Edge case: normalizeBlocks should not add a block to a Switch node
    Node switchNode = new Node(Token.SWITCH);
    switchNode.addChildToBack(statement);
    ast.normalizeBlocks(switchNode);
    assertFalse(switchNode.getFirstChild().isBlock());
    
  }

  @Test
  public void testPrepareAnnotations() {
    // Typical use case: prepareAnnotations should add annotations to call nodes
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    Node callNode = new Node(Token.CALL);
    annotations.annotateCalls(callNode);
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
    
    // Edge case: prepareAnnotations should add annotations to eval nodes
    Node evalNode = new Node(Token.NAME, "eval");
    Node callEval = new Node(Token.CALL, evalNode);
    annotations.annotateCalls(callEval);
    assertTrue(evalNode.getBooleanProp(Node.DIRECT_EVAL));
    
    // Typical use case: prepareAnnotations should add annotations to function nodes
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(functionNode);
    annotations.annotateDispatchers(functionNode, assignNode);
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }
  
}