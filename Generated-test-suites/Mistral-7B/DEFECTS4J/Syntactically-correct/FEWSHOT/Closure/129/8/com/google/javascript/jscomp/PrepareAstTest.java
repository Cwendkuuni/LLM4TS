package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = Node.objectLiteral();
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node controlStructure = Node.ifStatement();
    controlStructure.addChildToBack(Node.block().setWasEmptyNode(true));
    controlStructure.addChildToBack(Node.literal(true));
    root.addChildrenToFront(controlStructure);

    prepareAst.process(null, root);

    Node controlStructureBlock = controlStructure.getFirstChild();
    assertTrue(controlStructureBlock.isBlock());

    Node ifStatement = controlStructure.getPreviousSibling();
    assertFalse(ifStatement.isBlock());
  }

  @Test
  public void testNormalizeAnnotations() {
    Node objectLit = Node.objectLiteral();
    Node key = Node.name("a");
    Node value = Node.name("b");
    Node jsDoc = Node.jsDocComment("/** */").setParent(key);
    objectLit.addChildrenToFront(key);
    key.addChildToBack(value);
    value.addChildToBack(jsDoc);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, objectLit);

    assertTrue(value.getJSDocInfo() != null);
    assertNull(key.getJSDocInfo());
  }

  @Test
  public void testAnnotateCalls() {
    Node call = Node.callExpression(Node.name("eval"), Node.emptyList());

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, call);

    assertTrue(call.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    Node assign = Node.assign(Node.name("x"), Node.functionExpression(null, null, null));
    Node function = assign.getLastChild();

    Node jsDoc = Node.jsDocComment("/** @this {MyClass} */").setParent(assign);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(Node.objectLiteral(), assign);

    assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test(expected = AssertionError.class)
  public void checkChangeConvention() {
    Node root = Node.objectLiteral();
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    root.putBooleanProp(Node.FREE_CALL, true);
    prepareAst.reportChange();
  }
}