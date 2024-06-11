package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodePrinter;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;
  private CodePrinter printer;

  @Before
  public void setup() {
    consumer = mock(CodeConsumer.class);
    printer = mock(CodePrinter.class);
    when(consumer.continueProcessing()).thenReturn(true);
    when(consumer.append(anyString())).thenCallRealMethod();
    when(consumer.getLastChar()).thenCallRealMethod();
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
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("bar");
    consumer.append("baz");

    String expected = "barbaz";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    String expected = "{\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    String expected = "}\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    String expected = "\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    String expected = "\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    String expected = "\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    String expected = "{\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    String expected = "}";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndBlockWithNewline() {
    consumer.endBlock(true);
    String expected = "}";
    verify(printer, times(1)).print(expected);

    String expectedLineBreak = "\n";
    verify(printer, times(1)).print(expectedLineBreak);
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    String expected = ", ";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    String expected = "\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.endStatement(true);
    String expected = "\n;";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    String expected = "\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    String expected = "}\n";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
    String expected = "}\n";
    verify(printer, times(1)).print(expected);

    String expectedLineBreak = "\n";
    verify(printer, times(1)).print(expectedLineBreak);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    String expected = ": ";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testEndCaseBody() {
  }

  @Test
  public void testAdd() {
    consumer.add("hello");
    String expected = "hello";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    String expected = "foo";
    verify(printer, times(1)).print(expected);
  }

  @Test
  public void testAddOp() {
    consumer.addOp("+", false);
    verify(printer, times(1)).print("+");
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(10.0);
    String expected = "10.0";
    verify(printer, times(1)).print(expected);
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
  public void testBreakAfterBlockForFalse() {
    Node node = mock(Node.class);
    boolean statementContext = false;
    assertFalse(consumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
  }
}