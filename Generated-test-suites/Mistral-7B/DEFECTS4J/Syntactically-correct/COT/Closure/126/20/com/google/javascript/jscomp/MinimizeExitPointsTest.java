package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    minimizer = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_Label() {
    Node root = createNode(Token.LABEL, "label1");
    root.addChild(createNode(Token.BREAK, null));
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testMinimizeExitPoints_For() {
    Node root = createNode(Token.FOR, null);
    Node loopBlock = createNode(Token.BLOCK, null);
    root.addChild(loopBlock);
    loopBlock.addChild(createNode(Token.LABEL, "loopLabel"));
    loopBlock.addChild(createNode(Token.BREAK, "loopLabel"));
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testMinimizeExitPoints_While() {
    Node root = createNode(Token.WHILE, null);
    Node loopBlock = createNode(Token.BLOCK, null);
    root.addChild(loopBlock);
    loopBlock.addChild(createNode(Token.LABEL, "loopLabel"));
    loopBlock.addChild(createNode(Token.BREAK, "loopLabel"));
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testMinimizeExitPoints_DoWhile() {
    Node root = createNode(Token.DO, null);
    Node loopBlock = createNode(Token.BLOCK, null);
    Node condition = createNode(Token.IF, null);
    root.addChild(loopBlock);
    root.addChild(condition);
    loopBlock.addChild(createNode(Token.LABEL, "loopLabel"));
    loopBlock.addChild(createNode(Token.BREAK, "loopLabel"));
    condition.addChild(createNode(Token.BOOLEAN_LITERAL, false));
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_Function() {
    Node root = createNode(Token.FUNCTION, null);
    root.addChild(createNode(Token.RETURN, null));
    minimizer.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_TryCatch() {
    Node root = createNode(Token.TRY, null);
    Node tryBlock = createNode(Token.BLOCK, null);
    Node catchBlock = createNode(Token.BLOCK, null);
    root.addChild(tryBlock);
    root.addChild(catchBlock);
    tryBlock.addChild(createNode(Token.RETURN, null));
    minimizer.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_TryFinally() {
    Node root = createNode(Token.TRY, null);
    Node tryBlock = createNode(Token.BLOCK, null);
    Node finallyBlock = createNode(Token.BLOCK, null);
    root.addChild(tryBlock);
    root.addChild(finallyBlock);
    tryBlock.addChild(createNode(Token.RETURN, null));
    minimizer.process(null, root);
  }

  @Test
  public void testMinimizeExitPoints_If_TrueBlock_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock);
    ifBlock.addChild(exit);
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testMinimizeExitPoints_If_FalseBlock_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock);
    ifBlock.addChild(createNode(Token.BLOCK, null));
    ifBlock.getLastChild().addChild(exit);
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testMinimizeExitPoints_If_BothBlocks_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock1 = createNode(Token.BLOCK, null);
    Node ifBlock2 = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock1);
    root.addChild(ifBlock2);
    ifBlock1.addChild(exit);
    minimizer.process(null, root);
    assertNull(root.getLastChild());
  }

  private Node createNode(int type, String labelName) {
    Node node = IR.nodeFactory().createNode(type, compiler);
    if (labelName != null) {
      node.setLabel(labelName);
    }
    return node;
  }

  @Test
  public void testTryMinimizeIfBlockExits_TrueBlock_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock);
    ifBlock.addChild(exit);

    Node srcBlock = createNode(Token.BLOCK, null);
    ifBlock.addChild(srcBlock);
    srcBlock.addChild(createNode(Token.RETURN, null));

    Node destBlock = createNode(Token.BLOCK, null);
    ifBlock.addChild(destBlock);

    ArgumentCaptor<Node> newDestBlockCaptor = ArgumentCaptor.forClass(Node.class);
    minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, root, Token.RETURN, null);

    Node newDestBlock = newDestBlockCaptor.getValue();
    Node newDestParent = srcBlock.getParent();
    assertNull(newDestBlock.getParent());
    assertNull(newDestParent.getLastChild());
    newDestParent.addChildToBack(newDestBlock);
  }

  @Test
  public void testTryMinimizeIfBlockExits_FalseBlock_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock);
    ifBlock.addChild(createNode(Token.BLOCK, null));
    ifBlock.getLastChild().addChild(exit);

    Node srcBlock = createNode(Token.BLOCK, null);
    ifBlock.getLastChild().addChild(srcBlock);
    srcBlock.addChild(createNode(Token.RETURN, null));

    Node destBlock = createNode(Token.BLOCK, null);
    ifBlock.getLastChild().addChild(destBlock);

    minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, root, Token.RETURN, null);

    Node destParent = srcBlock.getParent();
    assertNull(destBlock.getParent());
    assertNull(destParent.getLastChild());
    destParent.addChildToBack(destBlock);
  }

  @Test
  public void testTryMinimizeIfBlockExits_BothBlocks_HasExit() {
    Node root = createNode(Token.IF, null);
    Node ifBlock1 = createNode(Token.BLOCK, null);
    Node ifBlock2 = createNode(Token.BLOCK, null);
    Node exit = createNode(Token.RETURN, null);
    root.addChild(ifBlock1);
    root.addChild(ifBlock2);
    ifBlock1.addChild(exit);

    Node srcBlock1 = createNode(Token.BLOCK, null);
    ifBlock1.addChild(srcBlock1);
    srcBlock1.addChild(createNode(Token.RETURN, null));

    Node srcBlock2 = createNode(Token.BLOCK, null);
    ifBlock2.addChild(srcBlock2);
    srcBlock2.addChild(createNode(Token.RETURN, null));

    Node destBlock = createNode(Token.BLOCK, null);
    ifBlock1.addChild(destBlock);

    minimizer.tryMinimizeIfBlockExits(srcBlock1, destBlock, root, Token.RETURN, null);

    Node destParent = srcBlock1.getParent();
    assertNull(destBlock.getParent());
    assertNull(destParent.getLastChild());
    destParent.addChildToBack(destBlock);
  }
}