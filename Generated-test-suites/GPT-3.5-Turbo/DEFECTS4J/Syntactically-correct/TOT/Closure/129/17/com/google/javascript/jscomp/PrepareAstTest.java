import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testProcess_MethodWithExterns_AddAnnotation() {
    // Test case for checking that the PrepareAst process method adds annotations to externs.
    // Set up the test case
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());

    // Perform the action
    prepareAst.process(externs, root);

    // Perform the assertion
    assertTrue(externs.getJSDocInfo() != null);
  }

  @Test
  public void testProcess_MethodWithRoot_AddAnnotation() {
    // Test case for checking that the PrepareAst process method adds annotations to the root node.
    // Set up the test case
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());

    // Perform the action
    prepareAst.process(externs, root);

    // Perform the assertion
    assertTrue(root.getJSDocInfo() != null);
  }

  @Test
  public void testNormalizeNodeTypes_NormalizeExprVoidToExprResult() {
    // Test case for checking that the normalizeNodeTypes method correctly normalizes EXPR_VOID to EXPR_RESULT.
    // Set up the test case
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    Node rootNode = createTestNode();

    // Perform the action
    prepareAst.normalizeNodeTypes(rootNode);

    // Perform the assertion
    assertEquals(Token.EXPR_RESULT, rootNode.getFirstChild().getType());
  }

  @Test
  public void testNormalizeBlocks_AddBlockToIf() {
    // Test case for checking that the normalizeBlocks method correctly adds a block to an IF statement.
    // Set up the test case
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    Node rootNode = createTestNode();

    // Perform the action
    prepareAst.normalizeBlocks(rootNode);

    // Perform the assertion
    assertEquals(Token.BLOCK, rootNode.getFirstChild().getType());
  }

  @Test
  public void testNormalizeBlocks_NoChangeToNonControlStructure() {
    // Test case for checking that the normalizeBlocks method does not make any changes to a non-control structure node.
    // Set up the test case
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    Node rootNode = createTestNode();

    // Perform the action
    prepareAst.normalizeBlocks(rootNode);

    // Perform the assertion
    assertEquals(Token.EXPR_VOID, rootNode.getFirstChild().getType());
  }

  @Test
  public void testPrepareAnnotations_NormalizeObjectLiteralAnnotations() {
    // Test case for checking that the PrepareAnnotations method correctly normalizes object literal annotations.
    // Set up the test case
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objectLitNode = createObjectLiteralNode();

    // Perform the action
    prepareAnnotations.normalizeObjectLiteralAnnotations(objectLitNode);

    // Perform the assertion
    assertNotNull(objectLitNode.getFirstChild().getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotations_AnnotateCalls_FreeCall() {
    // Test case for checking that the PrepareAnnotations method correctly annotates free calls.
    // Set up the test case
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node callNode = createCallNode();

    // Perform the action
    prepareAnnotations.annotateCalls(callNode);

    // Perform the assertion
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAnnotations_AnnotateCalls_DirectEval() {
    // Test case for checking that the PrepareAnnotations method correctly annotates direct calls to eval.
    // Set up the test case
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node callNode = createEvalCallNode();

    // Perform the action
    prepareAnnotations.annotateCalls(callNode);

    // Perform the assertion
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_AnnotateDispatchers() {
    // Test case for checking that the PrepareAnnotations method correctly annotates dispatchers.
    // Set up the test case
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node functionNode = createFunctionNode();
    Node parent = createAssignNode(functionNode);

    // Perform the action
    prepareAnnotations.annotateDispatchers(functionNode, parent);

    // Perform the assertion
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotations_SetJSDocInfoOnFunctionValue() {
    // Test case for checking that the PrepareAnnotations method correctly sets JSDocInfo on the function value in an object literal.
    // Set up the test case
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objectLitNode = createObjectLiteralNodeWithValue();

    // Perform the action
    prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objectLitNode, objectLitNode.getFirstChild(), objectLitNode.getFirstChild().getFirstChild());

    // Perform the assertion
    assertNotNull(objectLitNode.getFirstChild().getFirstChild().getJSDocInfo());
  }

  private Node createTestNode() {
    Node rootNode = new Node(Token.ROOT);
    Node childNode = new Node(Token.EXPR_VOID);
    rootNode.addChildToBack(childNode);
    return rootNode;
  }

  private Node createObjectLiteralNode() {
    Node objectLitNode = new Node(Token.OBJECTLIT);
    Node keyNode = new Node(Token.STRING_KEY, new Node(Token.NAME, "a"));
    Node valueNode = new Node(Token.STRING, "b");
    keyNode.addChildToBack(valueNode);
    objectLitNode.addChildToBack(keyNode);
    return objectLitNode;
  }

  private Node createCallNode() {
    Node callNode = new Node(Token.CALL);
    Node nameNode = new Node(Token.NAME, "foo");
    callNode.addChildToBack(nameNode);
    return callNode;
  }

  private Node createEvalCallNode() {
    Node callNode = new Node(Token.CALL);
    Node nameNode = new Node(Token.NAME, "eval");
    callNode.addChildToBack(nameNode);
    return callNode;
  }

  private Node createFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }

  private Node createAssignNode(Node childNode) {
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(childNode);
    return assignNode;
  }

  private Node createObjectLiteralNodeWithValue() {
    Node objectLitNode = new Node(Token.OBJECTLIT);
    Node keyNode = new Node(Token.STRING_KEY, new Node(Token.NAME, "a"));
    Node valueNode = new Node(Token.FUNCTION);
    keyNode.addChildToBack(valueNode);
    objectLitNode.addChildToBack(keyNode);
    return objectLitNode;
  }

}