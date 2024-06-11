package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = createNode("extern function foo() {}");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAset.process(null, root);

    // Check that the root node type has not been changed.
    assertTrue(root.getType() == Token.FUNCTION);

    Node functionNode = root.getFirstChild();
    assertTrue(functionNode.getType() == Token.FUNCTION);

    Node child = functionNode.getFirstChild();
    assertTrue(child.getType() == Token.BLOCK);
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = createNode("if (true) {} else {}");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    assertTrue(ifNode.isControlStructure());
    assertTrue(ifNode.getFirstChild().isBlock());
    assertTrue(ifNode.getNext().isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = createNode("/** @constructor */ function Foo() {}");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node constructorNode = root.getFirstChild();
    assertTrue(constructorNode.isFunction());
    assertTrue(constructorNode.getJSDocInfo() != null);
  }

  @Test
  public void testFreeCalls() {
    Node root = createNode("eval('function foo() {}')()");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testDirectEVAL() {
    Node root = createNode("eval('foo()')");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testDispatchers() {
    Node root = createNode("var obj = { __dispatchers: { foo: function() {} } }; obj.foo();");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node objectNode = root.getFirstChild();
    assertTrue(objectNode.isAssign());
    Node objectLiteralNode = objectNode.getFirstChild();
    assertTrue(objectLiteralNode.isObjectLit());
    Node dispatcherNode = objectLiteralNode.getFirstChild();
    assertTrue(dispatcherNode.isProperty());
    Node functionNode = dispatcherNode.getFirstChild();
    assertTrue(functionNode.isFunction());
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  private Node createNode(String code) {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = IR.parse(compiler, "", code).getFirstChild();
    Node root = externs;
    return root;
  }
}