package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertTrue(CodeConsumer.isWordChar('Z'));
    assertFalse(CodeConsumer.isWordChar('!'));
    assertFalse(CodeConsumer.isWordChar('@'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer cc = new StubCodeConsumer();
    assertTrue(cc.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer cc = new StubCodeConsumer();
    Node node = Node.newCompoundStatement();
    assertTrue(cc.breakAfterBlockFor(node, true));
    assertFalse(cc.breakAfterBlockFor(node, false));
  }

  static class StubCodeConsumer extends CodeConsumer {
    @Override
    void startSourceMapping(Node node) {
      // do nothing
    }

    @Override
    void endSourceMapping(Node node) {
      // do nothing
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    char getLastChar() {
      return 'a';
    }

    @Override
    void add(String newcode) {
      // do nothing
    }

    @Override
    void appendOp(String op, boolean binOp) {
      // do nothing
    }

    @Override
    void addOp(String op, boolean binOp) {
      // do nothing
    }

    @Override
    void addNumber(double x) {
      // do nothing
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return true;
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return true;
    }

    @Override
    void endFile() {
      // do nothing
    }
  }

}