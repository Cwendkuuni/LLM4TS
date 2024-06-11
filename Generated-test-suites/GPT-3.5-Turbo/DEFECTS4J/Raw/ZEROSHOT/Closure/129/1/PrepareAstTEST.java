import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
  
  private PrepareAst prepareAst;
  
  @Before
  public void setUp() {
    prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst = new PrepareAst(new AbstractCompiler(), true);
  }
  
  @Test
  public void testProcess_withNullExternsAndRoot_shouldNotThrowException() {
    try {
      prepareAst.process(null, null);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
  
  @Test
  public void testProcess_withNonNullExternsAndNullRoot_shouldNotThrowException() {
    try {
      prepareAst.process(new Node(), null);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
  
  @Test
  public void testProcess_withNonNullExternsAndRoot_shouldNotThrowException() {
    try {
      prepareAst.process(new Node(), new Node());
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
  
  @Test
  public void testNormalizeNodeTypes_withNullNode_shouldNotThrowException() {
    try {
      prepareAst.normalizeNodeTypes(null);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
  
  @Test
  public void testNormalizeNodeTypes_withValidNode_shouldNormalizeNodeTypes() {
    Node node = new Node();
    node.addChildToBack(new Node(Token.INIT));
    prepareAst.normalizeNodeTypes(node);
    
    Node expectedNode = new Node();
    expectedNode.addChildToBack(new Node(Token.BLOCK, new Node(Token.INIT)));
    
    assertEquals(expectedNode, node);
  }
  
  @Test
  public void testNormalizeBlocks_withNullNode_shouldNotThrowException() {
    try {
      prepareAst.normalizeBlocks(null);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
  
  @Test
  public void testNormalizeBlocks_withValidNode_shouldNormalizeBlocks() {
    Node node = new Node();
    node.addChildToBack(new Node(Token.IF, new Node(Token.BLOCK)));
    prepareAst.normalizeBlocks(node);
    
    Node expectedNode = new Node();
    expectedNode.addChildToBack(new Node(Token.IF, new Node(Token.BLOCK, new Node(Token.BLOCK))));
    
    assertEquals(expectedNode, node);
  }
  
  @Test
  public void testPrepareAnnotations_shouldNormalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING);
    key.addChildToFront(new Node(Token.NAME, new Node(Token.TYPEOFNAME, "a")));
    Node value = new Node(Token.STRING);
    value.addChildToFront(new Node(Token.NAME, new Node(Token.OBJECTLIT, "b")));
    key.addChildToBack(value);
    objlit.addChildToBack(key);
    prepareAst.PrepareAnnotations annotations = new prepareAst.PrepareAnnotations();
    annotations.normalizeObjectLiteralAnnotations(objlit);
    
    Node expectedObjlit = new Node(Token.OBJECTLIT);
    Node expectedKey = new Node(Token.STRING);
    expectedKey.addChildToFront(new Node(Token.NAME, new Node(Token.TYPEOFNAME, "a")));
    Node expectedValue = new Node(Token.STRING);
    expectedValue.addChildToFront(new Node(Token.NAME, new Node(Token.OBJECTLIT, "b")));
    expectedValue.setJSDocInfo(key.getJSDocInfo());
    expectedKey.addChildToBack(expectedValue);
    expectedObjlit.addChildToBack(expectedKey);
    
    assertEquals(expectedObjlit, objlit);
  }
  
  @Test
  public void testPrepareAnnotations_withCallNode_shouldAnnotateCalls() {
    Node callNode = new Node(Token.CALL);
    callNode.addChildToFront(new Node(Token.NAME, new Node(Token.OBJECTLIT, "a")));
    prepareAst.PrepareAnnotations annotations = new prepareAst.PrepareAnnotations();
    annotations.annotateCalls(callNode);
    
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }
  
  @Test
  public void testPrepareAnnotations_withEvalCall_shouldAnnotateEvalCalls() {
    Node evalCall = new Node(Token.NAME, new Node(Token.OBJECTLIT, "eval"));
    prepareAst.PrepareAnnotations annotations = new prepareAst.PrepareAnnotations();
    annotations.annotateCalls(evalCall);
    
    assertTrue(evalCall.getBooleanProp(Node.DIRECT_EVAL));
  }
  
  @Test
  public void testPrepareAnnotations_withFunctionNodeAndAssignmentParent_shouldAnnotateDispatchers() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(functionNode);
    prepareAst.PrepareAnnotations annotations = new prepareAst.PrepareAnnotations();
    annotations.annotateDispatchers(functionNode, parent);
    
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }
  
  @Test
  public void testPrepareAnnotations_withFunctionNodeAndNonAssignmentParent_shouldNotAnnotateDispatchers() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(functionNode);
    prepareAst.PrepareAnnotations annotations = new prepareAst.PrepareAnnotations();
    annotations.annotateDispatchers(functionNode, parent);
    
    assertFalse(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }
}