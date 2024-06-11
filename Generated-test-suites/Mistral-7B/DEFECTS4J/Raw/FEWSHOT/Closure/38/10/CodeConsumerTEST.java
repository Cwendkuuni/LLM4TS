package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('0'));
    assertFalse(consumer.isWordChar('!'));
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
    assertFalse(consumer.isNegativeZero(-1.0));
    assertFalse(consumer.isNegativeZero(1.2));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String code = "foo";
    consumer.add(code);
    char expected = code.charAt(code.length() - 1);
    char actual = consumer.getLastChar();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String code = "foo";
    consumer.append(code);
    String actual = consumer.getCode();
    assertEquals(code, actual);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    String actual = consumer.getCode();
    assertEquals("{\n", actual);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    String actual = consumer.getCode();
    assertEquals("}\n", actual);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    String actual = consumer.getCode();
    assertEquals("\n", actual);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    String actual = consumer.getCode();
    assertTrue(actual.matches("\\s*"));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    String actual = consumer.getCode();
    assertTrue(actual.matches("\\s*"));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    String actual = consumer.getCode();
    assertEquals("\n", actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    String actual = consumer.getCode();
    assertTrue(actual.matches("\\s*"));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    String actual = consumer.getCode();
    assertEquals("{\n", actual);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock();
    String actual = consumer.getCode();
    assertEquals("}\n", actual);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock(true);
    String actual = consumer.getCode();
    assertEquals("}\n", actual);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    String actual = consumer.getCode();
    assertEquals(",\n", actual);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement();
    String actual = consumer.getCode();
    assertTrue(actual.matches(";\\s*"));
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement(true);
    String actual = consumer.getCode();
    assertTrue(actual.matches(";\\s*\n"));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeEndStatement();
    String actual = consumer.getCode();
    assertTrue(actual.matches(";?\\s*"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction();
    String actual = consumer.getCode();
    assertTrue(actual.matches("}\\s*"));
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction(true);
    String actual = consumer.getCode();
    assertEquals("}\n", actual);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    String actual = consumer.getCode();
    assertEquals(":\n", actual);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endCaseBody();
    String actual = consumer.getCode();
    assertTrue(actual.matches("\\s*"));
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String code = "foo";
    consumer.add(code);
    String actual = consumer.getCode();
    assertEquals(code, actual);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String identifier = "foo";
    consumer.addIdentifier(identifier);
    String actual = consumer.getCode();
    assertEquals(identifier, actual);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String op = "+";
    consumer.addOp(op, true);
    String actual = consumer.getCode();
    assertEquals(op, actual);
  }

  @Test
  public void testAddOpWithSpace() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String op = "+";
    consumer.addOp(op, false);
    String actual = consumer.getCode();
    assertEquals(" ", actual);
    consumer.addOp(op, true);
    actual = consumer.getCode();
    assertEquals(op, actual);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    double x = 123.45;
    consumer.addNumber(x);
    String actual = consumer.getCode();
    assertEquals(String.valueOf(x), actual);
  }

  @Test
  public void testAddNumberWithNegativeSign() {
    CodeConsumer consumer = new DummyCodeConsumer();
    double x = -123.45;
    consumer.addNumber(x);
    String actual = consumer.getCode();
    assertEquals("-" + String.valueOf(Math.abs(x)), actual);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    String actual = consumer.getCode();
    assertTrue(actual.isEmpty());
  }

  static class DummyCodeConsumer extends CodeConsumer {
    @Override
    public char getLastChar() {
      return '\u0000';
    }

    @Override
    public String getCode() {
      StringBuilder sb = new StringBuilder();
      sb.append(super.getCode());
      return sb.toString();
    }
  }
}