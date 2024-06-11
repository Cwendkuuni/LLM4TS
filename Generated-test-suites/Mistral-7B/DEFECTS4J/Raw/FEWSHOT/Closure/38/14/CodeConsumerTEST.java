package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrueStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testBreakAfterBlockForFalseStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  static class TestCodeConsumer extends CodeConsumer {
    @Override
    void startSourceMapping(Node node) {
      super.startSourceMapping(node);
    }

    @Override
    void endSourceMapping(Node node) {
      super.endSourceMapping(node);
    }

    @Override
    abstract char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
    }

    @Override
    void append(String str) {
      super.append(str);
    }

    @Override
    void appendBlockStart() {
      super.appendBlockStart();
    }

    @Override
    void appendBlockEnd() {
      super.appendBlockEnd();
    }

    @Override
    void startNewLine() {
      super.startNewLine();
    }

    @Override
    void maybeLineBreak() {
      super.maybeLineBreak();
    }

    @Override
    void maybeCutLine() {
      super.maybeCutLine();
    }

    @Override
    void endLine() {
      super.endLine();
    }

    @Override
    void notePreferredLineBreak() {
      super.notePreferredLineBreak();
    }

    @Override
    void beginBlock() {
      super.beginBlock();
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      super.endBlock(shouldEndLine);
    }

    @Override
    void listSeparator() {
      super.listSeparator();
    }

    @Override
    void endStatement() {
      super.endStatement();
    }

    @Override
    void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
    }

    @Override
    void maybeEndStatement() {
      super.maybeEndStatement();
    }

    @Override
    void endFunction() {
      super.endFunction();
    }

    @Override
    void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
    }

    @Override
    void beginCaseBody() {
      super.beginCaseBody();
    }

    @Override
    void endCaseBody() {
      super.endCaseBody();
    }

    @Override
    void add(String newcode) {
      super.add(newcode);
    }

    @Override
    void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
    }

    @Override
    void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
    }

    @Override
    boolean isNegativeZero(double x) {
      return super.isNegativeZero(x);
    }

    @Override
    boolean isWordChar(char ch) {
      return super.isWordChar(ch);
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return true;
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return statementContext;
    }

    @Override
    void endFile() {
      super.endFile();
    }
  }
}