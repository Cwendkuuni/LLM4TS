package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumerTestBase;

public class CodeConsumerTest extends CodeConsumerTestBase {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createMockNode();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createMockNode();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = createTestConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    CodeConsumer consumer = createTestConsumerWithContinueProcessingFalse();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = createTestConsumer();
    consumer.addIdentifier("identifier");
    consumer.append(" = ");
    consumer.addNumber(1.0);
    consumer.endLine();
    assertEquals("identifier = 1.0\n", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = createTestConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = createTestConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.startNewLine();
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.maybeLineBreak();
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.maybeCutLine();
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.endLine();
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = createTestConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();
    assertEquals("{\n  foo\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock(true);
    assertEquals("{\n  foo\n}", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.listSeparator();
    consumer.append("bar");
    assertEquals("foo,\n  bar\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    assertEquals("foo;\nbar\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("{\n  function foo() {\n  }\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithSemicolon() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction(true);
    assertEquals("{\n  function foo() {\n  }\n}\n", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("switch (foo) {\n  case 1:\n");
    consumer.beginCaseBody();
    consumer.append("  bar();\n");
    consumer.endCaseBody();
    assertEquals("switch (foo) {\n  case 1:\n    bar();\n  }\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("foo");
    consumer.add(" bar");
    assertEquals("foo bar\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("x + y");
    consumer.addOp(" + ", true);
    assertEquals("x + y + \n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("x = ");
    consumer.addNumber(1.0);
    assertEquals("x = 1.0\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = createTestConsumer();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = createTestConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('1'));
    assertFalse(consumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = createTestConsumerWithShouldPreserveExtraBlocksTrue();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createMockNode();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createMockNode();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = createTestConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}