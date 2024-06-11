package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @org.junit.Before
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
  public void testContinueProcessingReturnsFalse() {
    consumer = mock(CodeConsumer.class);
    when(consumer.continueProcessing()).thenReturn(false);

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    // Test getting the last character of an empty codebase
    assertEquals('\u0000', consumer.getLastChar());

    // Test getting the last character after appending a string
    consumer.append("Hello");
    assertEquals('H', consumer.getLastChar());

    // Test getting the last character after appending a number
    consumer.addNumber(123);
    assertEquals('3', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("Hello World");
    assertEquals('W', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals('\n', consumer.getLastChar());
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
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    consumer.endBlock(true);
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(',', consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    consumer.endStatement(true);
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    assertEquals(':', consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
  }

  @Test
  public void testAdd() {
    consumer.add("Hello World");
    assertEquals('W', consumer.getLastChar());
  }

  @Test 
  public void testAddOp() {
    consumer.addOp("+", false);
    assertEquals('+', consumer.getLastChar());

    consumer.addOp("+", true);
    assertEquals('+', consumer.getLastChar());
  }

  @Test 
  public void testAddNumber() {
    consumer.addNumber(123);
    assertEquals('3', consumer.getLastChar());
  }

  @Test 
  public void testIsNegativeZero() {
    assertTrue(Consumer.isNegativeZero(-0.0));
  }

  @Test 
  public void testIsWordChar() {
    assertTrue(Consumer.isWordChar('_'));
    assertTrue(Consumer.isWordChar('$'));
    assertTrue(Consumer.isWordChar('a'));
    assertTrue(Consumer.isWordChar('1'));
    assertFalse(Consumer.isWordChar('!'));
  }

  @Test 
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test 
  public void testBreakAfterBlockForFalse() {
    assertTrue(consumer.breakAfterBlockFor(mock(Node.class), false));
  }

  @Test 
  public void testBreakAfterBlockForTrue() {
    assertTrue(consumer.breakAfterBlockFor(mock(Node.class), true));
  }

  @Test 
  public void testEndFile() {
    consumer.endFile();
  }
}