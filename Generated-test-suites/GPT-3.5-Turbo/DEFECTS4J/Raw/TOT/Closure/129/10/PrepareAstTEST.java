import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst();
  }

  @Test
  public void testProcess_WithNullExternsAndRoot_ShouldNotThrowException() {
    // Arrange
    Node externs = null;
    Node root = null;

    // Act
    prepareAst.process(externs, root);

    // Assert
    Assert.assertTrue(true);
  }

  @Test
  public void testProcess_WithNonNullExternsAndRoot_ShouldNotThrowException() {
    // Arrange
    Node externs = new Node(Token.FUNCTION);
    Node root = new Node(Token.FUNCTION);

    // Act
    prepareAst.process(externs, root);

    // Assert
    Assert.assertTrue(true);
  }

  @Test
  public void testNormalizeNodeTypes_WithEmptyNode_ShouldNotThrowException() {
    // Arrange
    Node node = new Node(Token.EMPTY);

    // Act
    prepareAst.normalizeNodeTypes(node);

    // Assert
    Assert.assertTrue(true);
  }

  @Test
  public void testNormalizeNodeTypes_WithBlockNode_ShouldNormalizeBlocks() {
    // Arrange
    Node node = new Node(Token.BLOCK);
    Node child1 = new Node(Token.EXPR_VOID);
    Node child2 = new Node(Token.FALSE);

    node.addChildToBack(child1);
    node.addChildToBack(child2);

    // Act
    prepareAst.normalizeNodeTypes(node);

    // Assert
    Assert.assertEquals(Token.EXPR_RESULT, child1.getToken());
    Assert.assertEquals(child1.getOnlyChild(), child2);
  }

  @Test
  public void testNormalizeBlocks_WithIfStatementBlockNode_ShouldAddBlocks() {
    // Arrange
    Node ifStatement = new Node(Token.IF);
    Node block = new Node(Token.BLOCK);
    Node statement = new Node(Token.EXPR_VOID);

    ifStatement.addChildToBack(block);
    block.addChildToBack(statement);

    // Act
    prepareAst.normalizeBlocks(ifStatement);

    // Assert
    Assert.assertEquals(Token.BLOCK, block.getToken());
    Assert.assertEquals(block.getOnlyChild(), statement);
  }

  @Test
  public void testPrepareAnnotations_WithObjectLiteralNode_ShouldNormalizeObjectLiteralAnnotations() {
    // Arrange
    Node objectLiteral = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING);
    Node value1 = new Node(Token.STRING);
    Node key2 = new Node(Token.STRING);
    Node value2 = new Node(Token.FUNCTION);

    objectLiteral.addChildToBack(key1);
    key1.addChildToBack(value1);
    objectLiteral.addChildToBack(key2);
    key2.addChildToBack(value2);

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objectLiteral);

    // Assert
    Assert.assertNull(key1.getJSDocInfo());
    Assert.assertNotNull(value1.getJSDocInfo());
    Assert.assertNull(key2.getJSDocInfo());
    Assert.assertNotNull(value2.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotations_WithCallNodeWithoutThisValue_ShouldAnnotateCallAsFreeCall() {
    // Arrange
    Node callNode = new Node(Token.CALL);
    Node nameNode = new Node(Token.NAME, "someFunction");

    callNode.addChildToBack(nameNode);

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(callNode);

    // Assert
    Assert.assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAnnotations_WithCallNodeWithEval_ShouldAnnotateCallAsDirectEval() {
    // Arrange
    Node callNode = new Node(Token.CALL);
    Node nameNode = new Node(Token.NAME, "eval");

    callNode.addChildToBack(nameNode);

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(callNode);

    // Assert
    Assert.assertTrue(nameNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_WithFunctionNodeInsideAssignNode_ShouldAnnotateFunctionAsDispatcher() {
    // Arrange
    Node assignNode = new Node(Token.ASSIGN);
    Node functionNode = new Node(Token.FUNCTION);

    assignNode.addChildToBack(functionNode);

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateDispatchers(functionNode, assignNode);

    // Assert
    Assert.assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

}