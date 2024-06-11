import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new IR.program();
    prepareAst.process(null, root);

    assertNotNull(root.getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_CheckOnly_NormalizeNodeTypeConstraintViolated() {
    PrepareAst prepareAst = new PrepareAst(null, true);

    Node root = new IR.program();
    prepareAst.process(null, root);

    fail("NormalizeNodeType constraints should have been violated.");
  }

  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node node = new IR.ifStatement(new IR.booleanLiteral(true), new IR.block(), null);

    prepareAst.normalizeNodeTypes(node);

    assertNotNull(node.getFirstChild());
    assertNotNull(node.getLastChild());
    assertTrue(node.getLastChild().isBlock());
  }

  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node node = new IR.ifStatement(new IR.booleanLiteral(true), null, new IR.callExpression(new IR.name("print"), null));

    prepareAst.normalizeBlocks(node);

    assertNotNull(node.getFirstChild());
    assertNotNull(node.getLastChild());
    assertTrue(node.getLastChild().isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node objLit = new IR.objectLiteral();
    objLit.setJSDocInfo(new JSDocInfo("MyObject", null, null, null, null));

    Node key1 = new IR.stringLiteral("prop1");
    Node value1 = new IR.numberLiteral(1);
    Node prop1 = new IR.property(key1, value1);
    objLit.addChild(prop1);

    Node key2 = new IR.stringLiteral("prop2");
    Node value2 = new IR.function(null, new IR.list(), null);
    Node prop2 = new IR.property(key2, value2);
    objLit.addChild(prop2);

    Node annotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(new AbstractCompiler(), objLit, annotations);

    JSDocInfo expectedJsDoc = new JSDocInfo("MyObject", null, null, null, null);
    assertEquals(expectedJsDoc, value1.getJSDocInfo());
    assertEquals(expectedJsDoc, value2.getJSDocInfo());
  }

  @Test
  public void testAnnotateCalls() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node call = new IR.callExpression(new IR.name("eval"), null);

    prepareAst.annotateCalls(call);

    assertTrue(call.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node assign = new IR.assign(new IR.name("x"), new IR.function(null, null, null));

    prepareAst.annotateDispatchers(assign, new IR.name("MyClass"));

    assertTrue(assign.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node objLit = new IR.objectLiteral();
    Node key1 = new IR.stringLiteral("prop1");
    Node value1 = new IR.numberLiteral(1);
    objLit.addChild(new IR.property(key1, value1));

    Node key2 = new IR.stringLiteral("prop2");
    Node value2 = new IR.name("MyFunction");
    objLit.addChild(new IR.property(key2, value2));

    prepareAst.normalizeObjectLiteralAnnotations(objLit);

    assertEquals(value1.getJSDocInfo(), value2.getJSDocInfo());
  }
}