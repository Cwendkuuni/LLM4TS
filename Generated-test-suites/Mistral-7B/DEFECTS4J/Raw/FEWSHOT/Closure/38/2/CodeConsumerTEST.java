package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    when(consumer.continueProcessing()).thenReturn(true);
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    when(consumer.continueProcessing()).thenReturn(false);
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertEquals('\u0000', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    consumer.append("test");
    assertEquals("\ntest", consumer.code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.code.substring(consumer.code.length() - 2));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("\n", consumer.code.substring(consumer.code.length() - 2));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    consumer.append("test");
    assertEquals("\ntest", consumer.code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.code.substring(consumer.code.length() - 2));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.code.substring(consumer.code.length() - 3));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test", consumer.code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.code);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", false);
    assertEquals("test+", consumer.code);
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    assertEquals("test +", consumer.code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(42);
    assertEquals("42", consumer.code);
  }

  @Test
  public void testAddNumberWithNegativeZero() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(-0.0);
    assertEquals("-0.0", consumer.code);
  }

  @Test
  public void testAddNumberWithLargeInt() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(Long.MAX_VALUE);
    assertEquals(Long.toString(Long.MAX_VALUE), consumer.code);
  }

  @Test
  public void testAddNumberWithFloat() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(3.14159265);
    assertEquals("3.14159265", consumer.code);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('0'));
    assertFalse(consumer.isWordChar('+'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.code);
  }

  private static class StubCodeConsumer extends CodeConsumer {
    String code = "";
  }
}