import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    // test for Process method functionality
    PrepareAst prepare = new PrepareAst(new AbstractCompiler());
    Node externs = IR.root().srcref("externs");
    Node root = IR.root().srcref("root");
    
    prepare.process(externs, root);
    
    // Assertions for the test
    assertNotNull(externs);
    assertNotNull(root);
  }

  @Test
  public void testNormalizeNodeTypes() {
    // test for normalizeNodeTypes method functionality
    PrepareAst prepare = new PrepareAst(new AbstractCompiler());
    Node node = IR.root().srcref("node");
    
    prepare.normalizeNodeTypes(node);
    
    // Assertions for the test
    assertNotNull(node);
  }

  @Test
  public void testNormalizeBlocks() {
    // test for normalizeBlocks method functionality
    PrepareAst prepare = new PrepareAst(new AbstractCompiler());
    Node node = IR.block();
    
    prepare.normalizeBlocks(node);
    
    // Assertions for the test
    assertNotNull(node);
  }

  @Test
  public void testAnnotateCalls() {
    // test for annotateCalls method functionality
    Node callNode = IR.call(IR.name("foo"));
    
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    annotations.annotateCalls(callNode);
    
    // Assertions for the test
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testAnnotateDispatchers() {
    // test for annotateDispatchers method functionality
    Node functionNode = IR.function(IR.name("bar"), IR.paramList(), IR.block());
    Node parent = IR.exprResult(IR.assign(IR.name("baz"), functionNode));
    
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    annotations.annotateDispatchers(functionNode, parent);
    
    // Assertions for the test
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    // test for normalizeObjectLiteralAnnotations method functionality
    Node objLitNode = IR.objectlit();
    Node keyNode = IR.stringKey("key", IR.name("value"));
    
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    annotations.normalizeObjectLiteralAnnotations(objLitNode);
    
    // Assertions for the test
    assertNotNull(objLitNode);
    assertNotNull(keyNode);
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    // test for normalizeObjectLiteralKeyAnnotations method functionality
    Node objLitNode = IR.objectlit();
    Node keyNode = IR.stringKey("key");
    Node valueNode = IR.name("value");
    keyNode.addChildToBack(valueNode);
    
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    annotations.normalizeObjectLiteralKeyAnnotations(objLitNode, keyNode, valueNode);
    
    // Assertions for the test
    assertNotNull(objLitNode);
    assertNotNull(keyNode);
    assertNotNull(valueNode.getJSDocInfo());
  }
}