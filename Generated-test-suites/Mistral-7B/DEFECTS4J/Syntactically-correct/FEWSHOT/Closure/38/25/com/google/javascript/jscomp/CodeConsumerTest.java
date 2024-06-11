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
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  static class TestCodeConsumer extends CodeConsumer {
    // Add test methods for all abstract and non-abstract methods in CodeConsumer here.

    @Override
    public void startSourceMapping(Node node) {
      // Add test methods for startSourceMapping and endSourceMapping here.
    }

    @Override
    public void endSourceMapping(Node node) {
      // Add test methods for startSourceMapping and endSourceMapping here.
    }

    @Override
    public boolean continueProcessing() {
      // Add test methods for continueProcessing here.
      return true;
    }

    @Override
    public char getLastChar() {
      // Add test methods for getLastChar here.
      return ' ';
    }

    @Override
    public void addIdentifier(String identifier) {
      // Add test methods for addIdentifier here.
    }

    @Override
    public void append(String str) {
      // Add test methods for append here.
    }

    @Override
    public void appendBlockStart() {
      // Add test methods for appendBlockStart and appendBlockEnd here.
    }

    @Override
    public void appendBlockEnd() {
      // Add test methods for appendBlockStart and appendBlockEnd here.
    }

    @Override
    public void startNewLine() {
      // Add test methods for startNewLine and endLine here.
    }

    @Override
    public void maybeLineBreak() {
      // Add test methods for maybeLineBreak and maybeCutLine here.
    }

    @Override
    public void maybeCutLine() {
      // Add test methods for maybeLineBreak and maybeCutLine here.
    }

    @Override
    public void endLine() {
      // Add test methods for startNewLine and endLine here.
    }

    @Override
    public void notePreferredLineBreak() {
      // Add test methods for notePreferredLineBreak here.
    }

    @Override
    public void beginBlock() {
      // Add test methods for beginBlock and endBlock here.
    }

    @Override
    public void endBlock() {
      // Add test methods for beginBlock and endBlock here.
    }

    @Override
    public void listSeparator() {
      // Add test methods for listSeparator here.
    }

    @Override
    public void endStatement() {
      // Add test methods for endStatement and maybeEndStatement here.
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      // Add test methods for endStatement and maybeEndStatement here.
    }

    @Override
    public void maybeEndStatement() {
      // Add test methods for endStatement and maybeEndStatement here.
    }

    @Override
    public void endFunction() {
      // Add test methods for endFunction and endFunction here.
    }

    @Override
    public void endFunction(boolean statementContext) {
      // Add test methods for endFunction and endFunction here.
    }

    @Override
    public void beginCaseBody() {
      // Add test methods for beginCaseBody and endCaseBody here.
    }

    @Override
    public void endCaseBody() {
      // Add test methods for beginCaseBody and endCaseBody here.
    }

    @Override
    public void add(String newcode) {
      // Add test methods for add here.
    }

    @Override
    public void appendOp(String op, boolean binOp) {
      // Add test methods for appendOp and addOp here.
    }

    @Override
    public void addOp(String op, boolean binOp) {
      // Add test methods for addOp and addOp here.
    }

    @Override
    public void addNumber(double x) {
      // Add test methods for addNumber here.
    }

    @Override
    public static boolean isNegativeZero(double x) {
      // Add test methods for isNegativeZero here.
      return false;
    }

    @Override
    public static boolean isWordChar(char ch) {
      // Add test methods for isWordChar here.
      return true;
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      // Add test methods for shouldPreserveExtraBlocks here.
      return true;
    }

    @Override
    public boolean breakAfterBlockFor(Node n, boolean statementContext) {
      // Add test methods for breakAfterBlockFor here.
      return true;
    }

    @Override
    public void endFile() {
      // Add test methods for endFile here.
    }
  }
}