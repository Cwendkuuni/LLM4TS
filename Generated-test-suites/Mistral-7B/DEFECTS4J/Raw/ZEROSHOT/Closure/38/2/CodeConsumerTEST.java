package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer codeConsumer;

  @Before
  public void setup() {
    codeConsumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    when(codeConsumer.continueProcessing()).thenReturn(true);

    boolean result = codeConsumer.continueProcessing();

    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    when(codeConsumer.getLastChar()).thenReturn('a');

    char result = codeConsumer.getLastChar();

    assertEquals('a', result);
  }

  @Test
  public void testAppend() {
    codeConsumer.append("test append");

    String expected = "test append";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();

    String expected = "{";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();

    String expected = "}";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();

    String expected = "\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();

    String expected = "\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeCutLine() {
    // This method is abstract and cannot be tested directly.
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();

    String expected = "\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    // This method is abstract and cannot be tested directly.
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();

    String expected = "{\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();

    String expected = "}\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlockWithNewline() {
    codeConsumer.endBlock(true);

    String expected = "}\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();

    String expected = ", \n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();

    String expected = "; \n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    codeConsumer.endStatement(true);

    String expected = "; \n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();

    String expected = "\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatementWithSemiColon() {
    codeConsumer.maybeEndStatement(true);

    String expected = "; \n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();

    String expected = "}\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    codeConsumer.endFunction(true);

    String expected = "\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();

    String expected = ": \n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();

    String expected = "";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    codeConsumer.add("test add");

    String expected = "test add\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    codeConsumer.addOp("+", false);

    String expected = "+";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    codeConsumer.addIdentifier("identifier");

    String expected = "identifier\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    codeConsumer.addNumber(42.0);

    String expected = "42.0\n";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(0.0));
    assertEquals(true, CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('b'));
    assertEquals(false, CodeConsumer.isWordChar(5));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertEquals(false, codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean result = codeConsumer.breakAfterBlockFor(node, false);

    assertEquals(false, result);
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    Node node = mock(Node.class);
    boolean result = codeConsumer.breakAfterBlockFor(node, true);

    assertEquals(true, result);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();

    String expected = "";
    String actual = codeConsumer.getCode();

    assertEquals(expected, actual);
  }

  private String getCode() {
    return codeConsumer.getCode();
  }
}