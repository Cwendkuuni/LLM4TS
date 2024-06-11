package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    throw new UnsupportedOperationException("This method should be implemented by subclasses.");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAppend() {
    throw new UnsupportedOperationException("This method should be implemented by subclasses.");
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    consumer.endBlock(true);
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithNeedSemiColon() {
    consumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    String newcode = "test";
    consumer.add(newcode);

    boolean isSpaceAfterLastChar = getLastCharIsSpace(consumer.getLastChar());
    assertTrue(isSpaceAfterLastChar);

    consumer.add(newcode);

    isSpaceAfterLastChar = getLastCharIsSpace(consumer.getLastChar());
    assertFalse(isSpaceAfterLastChar);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    consumer.addOp(op, true);

    char lastChar = consumer.getLastChar();
    assertTrue(lastChar == '+' || lastChar == ' ');
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(1);
    consumer.addNumber(1.1);
    consumer.addNumber(-1);
    consumer.addNumber(-1.1);

    char lastChar = consumer.getLastChar();
    assertTrue(Character.isDigit(lastChar));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean statementContext = true;

    assertTrue(consumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
  }

  private boolean getLastCharIsSpace(char lastChar) {
    return lastChar == ' ';
  }
}