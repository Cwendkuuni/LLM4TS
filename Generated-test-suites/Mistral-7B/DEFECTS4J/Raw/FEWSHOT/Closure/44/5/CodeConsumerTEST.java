package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumerTest;

public class CodeConsumerTest extends CodeConsumer {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new CodeConsumerTest();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new CodeConsumerTest();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumerTest();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.addIdentifier("foo");
    String code = consumer.getCode();
    assertEquals("foo", code);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    String code = consumer.getCode();
    assertEquals("foo", code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.appendBlockStart();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.appendBlockEnd();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.startNewLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.maybeLineBreak();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.maybeCutLine();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.endLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.notePreferredLineBreak();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginBlock();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginBlock();
    consumer.endBlock();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginBlock();
    consumer.endBlock(true);
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.listSeparator();
    String code = consumer.getCode();
    assertEquals(",\n", code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.endStatement();
    String code = consumer.getCode();
    assertEquals("foo;\n", code);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.endStatement(true);
    String code = consumer.getCode();
    assertEquals("foo;\n", code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.maybeEndStatement();
    String code = consumer.getCode();
    assertEquals("foo", code);
  }

  @Test
  public void testMaybeEndStatementWithSemiColon() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.maybeEndStatement(true);
    String code = consumer.getCode();
    assertEquals("foo;\n", code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.endFunction();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endFunction(true);
    String code = consumer.getCode();
    assertEquals("}\nfoo;\n", code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginCaseBody();
    String code = consumer.getCode();
    assertEquals(":\n", code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.add("bar");
    String code = consumer.getCode();
    assertEquals("foobar\n", code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.addOp("+", true);
    consumer.append("bar");
    String code = consumer.getCode();
    assertEquals("foo + bar\n", code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.append("foo");
    consumer.addNumber(42.0);
    String code = consumer.getCode();
    assertEquals("foo 42.0\n", code);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new CodeConsumerTest();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new CodeConsumerTest();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('1'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumerTest();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumerTest();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.endFile();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new CodeConsumerTest();
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private String getCode() {
    return codeBuffer.getCode();
  }
}