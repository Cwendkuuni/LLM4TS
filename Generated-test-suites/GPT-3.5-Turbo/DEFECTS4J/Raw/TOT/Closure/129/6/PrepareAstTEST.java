import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class PrepareAstTest {
  
  @Test
  public void testProcess_NullExternsAndRoot_ShouldNotThrowException() {
    PrepareAst prepareAst = new PrepareAst(null);
    try {
      prepareAst.process(null, null);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testProcess_CheckOnly_NullExternsAndRoot_ShouldThrowException() {
    PrepareAst prepareAst = new PrepareAst(null, true);
    try {
      prepareAst.process(null, null);
      assert false;
    } catch(IllegalStateException e) {
      assert true;
    }
  }
  
  @Test
  public void testProcess_NonNullExternsAndNullRoot_ShouldNotThrowException() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node externs = IR.var(IR.name("x"), IR.number(1));
    try {
      prepareAst.process(externs, null);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testProcess_NullRootAndNonEmptyExterns_ShouldTraverseExterns() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node externs = IR.var(IR.name("x"), IR.number(1));
    try {
      prepareAst.process(externs, null);
      assertTrue(prepareAst.isTraversingExterns());
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testProcess_NonNullRootAndExterns_ShouldTraverseRoot() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node root = IR.var(IR.name("x"), IR.number(1));
    try {
      prepareAst.process(null, root);
      assertFalse(prepareAst.isTraversingExterns());
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testNormalizeNodeTypes_NullParent_NodeTypeCheck_ShouldNotThrowException() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node node = IR.number(1);
    try {
      prepareAst.normalizeNodeTypes(node);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testNormalizeNodeTypes_ParentNodeCheck_ShouldNotThrowException() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node parent = new Node(Token.VAR);
    Node node = IR.number(1);
    parent.addChildToBack(node);
    node.setParent(parent);
    try {
      prepareAst.normalizeNodeTypes(parent);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testNormalizeBlocks_InvalidControlToken_ShouldNotThrowException() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node node = new Node(Token.BLOCK);
    try {
      prepareAst.normalizeBlocks(node);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testNormalizeBlocks_ControlStructureAndCodeBlock_ShouldNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(null);
    Node node = new Node(Token.IF);
    Node codeBlock = IR.block(IR.empty());
    node.addChildToBack(codeBlock);
    try {
      prepareAst.normalizeBlocks(node);
      assertEquals(node.getFirstChild(), codeBlock);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testPrepareAnnotations_ShouldNormalizeObjectLiteralAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objectLiteral = IR.objectlit(IR.stringKey("a", IR.string("b")));
    try {
      prepareAnnotations.normalizeObjectLiteralAnnotations(objectLiteral);
      assertEquals(objectLiteral.getJSDocInfo(), objectLiteral.getFirstChild().getJSDocInfo());
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testPrepareAnnotations_NormalizeObjectLiteralKeyAnnotations_ShouldWork() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objectLiteral = IR.objectlit(IR.stringKey("a", IR.function(IR.name(""), IR.block())));
    try {
      prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objectLiteral, objectLiteral.getFirstChild(), objectLiteral.getFirstChild().getFirstChild());
      assertEquals(objectLiteral.getFirstChild().getJSDocInfo(), objectLiteral.getFirstChild().getFirstChild().getJSDocInfo());
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testPrepareAnnotations_annotateCalls_ShouldAnnotateCalls() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node call = new Node(Token.CALL);
    Node identifier = IR.name("myFunction");
    call.addChildToBack(identifier);
    try {
      prepareAnnotations.annotateCalls(call);
      assertTrue(call.getBooleanProp(Node.FREE_CALL));
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testPrepareAnnotations_annotateDispatchers_ShouldAnnotateDispatchers() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(function);
    try {
      prepareAnnotations.annotateDispatchers(function, parent);
      assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
  @Test
  public void testPrepareAnnotations_SwitchStatementNode_ShouldNotThrowException() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node switchNode = new Node(Token.SWITCH);
    try {
      prepareAnnotations.shouldTraverse(null, switchNode, null);
    } catch(Exception e) {
      assert false;
    }
    assert true;
  }
  
}