
import org.junit.Assert;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeBlocks() {
    PrepareAst ast = new PrepareAst(new AbstractCompiler());
    // Create a test case with a control structure node
    Node testNode = new Node(Token.IF);
    // Add a child node that is not a block
    Node childNode = new Node(Token.SCRIPT);
    testNode.addChildToFront(childNode);

    // Call normalizeBlocks method
    ast.normalizeBlocks(testNode);

    // Assert that the child node is replaced with a block node
    Assert.assertTrue(testNode.getFirstChild().isBlock());
  }

  @Test
  public void testPrepareAnnotations_shouldTraverseObjectLit() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with an object literal node
    Node objectLit = new Node(Token.OBJECTLIT);
    // Add properties with JSDoc annotations
    Node property1 = new Node(Token.STRING_KEY);
    property1.setJSDocInfo(new JSDocInfo());
    objectLit.addChildToFront(property1);
    Node property2 = new Node(Token.STRING_KEY);
    property2.setJSDocInfo(new JSDocInfo());
    objectLit.addChildToFront(property2);

    // Call shouldTraverse method
    boolean result = annotations.shouldTraverse(null, objectLit, null);

    // Assert that the result is true
    Assert.assertTrue(result);
  }

  @Test
  public void testPrepareAnnotations_visitCall_withFreeCall() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with a call node
    Node callNode = new Node(Token.CALL);
    // Add a child node that is not a get node
    Node childNode = new Node(Token.NAME);
    childNode.setString("method");
    callNode.addChildToFront(childNode);

    // Call annotateCalls method
    annotations.annotateCalls(callNode);

    // Assert that the call node has the FREE_CALL property
    Assert.assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAnnotations_visitCall_withDirectEval() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with a call node to eval
    Node evalNode = new Node(Token.CALL);
    // Set the call node to eval
    evalNode.addChildToFront(new Node(Token.NAME).setString("eval"));

    // Call annotateCalls method
    annotations.annotateCalls(evalNode);

    // Assert that the eval node has the DIRECT_EVal property
    Assert.assertTrue(evalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_visitFunction_withDispatcher() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with a function node
    Node functionNode = new Node(Token.FUNCTION);
    // Set the parent node to have a JSDocInfo with isJavaDispatch = true
    Node parentNode = new Node(Token.ASSIGN);
    parentNode.setJSDocInfo(new JSDocInfoBuilder().withJavaDispatch(true).build());
    functionNode.setParent(parentNode);

    // Call annotateDispatchers method
    annotations.annotateDispatchers(functionNode, parentNode);

    // Assert that the function node has the IS_DISPATCHER property
    Assert.assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotations_visitFunction_withoutDispatcher() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with a function node
    Node functionNode = new Node(Token.FUNCTION);
    // Set the parent node to have a JSDocInfo without isJavaDispatch
    Node parentNode = new Node(Token.ASSIGN);
    parentNode.setJSDocInfo(new JSDocInfoBuilder().build());
    functionNode.setParent(parentNode);

    // Call annotateDispatchers method
    annotations.annotateDispatchers(functionNode, parentNode);

    // Assert that the function node does not have the IS_DISPATCHER property
    Assert.assertFalse(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotations_normalizeObjectLiteralKeyAnnotations() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    // Create a test case with an object literal node
    Node objectLit = new Node(Token.OBJECTLIT);
    // Add a property node with a JSDoc annotation
    Node property = new Node(Token.STRING_KEY);
    property.setJSDocInfo(new JSDocInfo());
    objectLit.addChildToFront(property);
    // Add a value node that is a function
    Node value = new Node(Token.FUNCTION);
    property.addChildToFront(value);

    // Call normalizeObjectLiteralKeyAnnotations method
    annotations.normalizeObjectLiteralKeyAnnotations(objectLit, property, value);

    // Assert that the value node has the JSDoc annotation
    Assert.assertNotNull(value.getJSDocInfo());
  }

  @Test
  public void testProcess_shouldNormalizeNodeTypes() {
    PrepareAst ast = new PrepareAst(new AbstractCompiler(true));

    // Create a test case with a block node
    Node blockNode = new Node(Token.BLOCK);

    // Add a control structure node
    Node controlStructure = new Node(Token.IF);
    blockNode.addChildToFront(controlStructure);

    // Add a child node that is not a block
    Node childNode = new Node(Token.SCRIPT);
    controlStructure.addChildToFront(childNode);

    // Call process method
    ast.process(null, blockNode);

    // Assert that the child node is replaced with a block node
    Assert.assertTrue(controlStructure.getFirstChild().isBlock());
  }

}
