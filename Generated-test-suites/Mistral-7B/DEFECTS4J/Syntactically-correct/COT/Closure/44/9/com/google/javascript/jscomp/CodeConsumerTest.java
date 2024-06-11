package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for CodeConsumer.
 */
public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method was called
    assertTrue(consumer.wasStartSourceMappingCalled);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Check if endSourceMapping method was called
    assertTrue(consumer.wasEndSourceMappingCalled);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    // Test continueProcessing method with default behavior
    assertTrue(consumer.continueProcessing());

    consumer = new InterruptingCodeConsumer();
    // Test continueProcessing method with custom behavior
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("string");
    assertEquals("string", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // Cannot assert the result directly, as it depends on the implementation
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // Cannot assert the result directly, as it depends on the implementation
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot assert the result directly, as it depends on the implementation
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    // Cannot assert the result directly, as it depends on the implementation
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertTrue(sawFunction);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    // Cannot assert the result directly, as it depends on the implementation
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("string");
    assertEquals("string", consumer.getCode());
  }

  @Test
  public void testAddWithPreviousCharSpaced() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("return ");
    consumer.add("foo");
    assertEquals("return foo\n", consumer.getCode());
  }

  @Test
  public void testAddWithPreviousCharNotSpaced() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("return");
    consumer.add("foo");
    assertEquals("return foo\n", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testAppendWithInvalidCharacter() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("\u0000");
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(123);
    assertEquals("123\n", consumer.getCode());
  }

  @Test
  public void testAddNumberWithNegativeSign() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(-123);
    assertEquals("-123\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // Cannot assert the result directly, as it depends on the implementation
  }
}

class StubCodeConsumer extends CodeConsumer {
  String code = "";
  boolean wasStartSourceMappingCalled = false;
  boolean wasEndSourceMappingCalled = false;

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public void append(String str) {
    code += str;
  }

  @Override
  public void appendBlockStart() {
    // Intentionally empty
  }

  @Override
  public void appendBlockEnd() {
    // Intentionally empty
  }

  @Override
  public void startNewLine() {
    // Intentionally empty
  }

  @Override
  public void maybeLineBreak() {
    // Intentionally empty
  }

  @Override
  public void maybeCutLine() {
    // Intentionally empty
  }

  @Override
  public void endLine() {
    // Intentionally empty
  }

  @Override
  public void notePreferredLineBreak() {
    // Intentionally empty
  }

  @Override
  public void beginBlock() {
    // Intentionally empty
  }

  @Override
  public void endBlock(boolean shouldEndLine) {
    // Intentionally empty
  }

  @Override
  public void listSeparator() {
    // Intentionally empty
  }

  @Override
  public void endStatement() {
    // Intentionally empty
  }

  @Override
  public void endStatement(boolean needSemiColon) {
    // Intentionally empty
  }

  @Override
  public void maybeEndStatement() {
    // Intentionally empty
  }

  @Override
  public void endFunction() {
    // Intentionally empty
  }

  @Override
  public void beginCaseBody() {
    // Intentionally empty
  }

  @Override
  public void endCaseBody() {
    // Intentionally empty
  }

  @Override
  public void add(String newcode) {
    code += newcode;
  }

  @Override
  public void appendOp(String op, boolean binOp) {
    code += op;
  }

  @Override
  public void addOp(String op, boolean binOp) {
    code += op;
  }

  @Override
  public void addNumber(double x) {
    code += String.valueOf(x);
    code += "\n";
  }

  @Override
  public boolean shouldPreserveExtraBlocks() {
    return true;
  }

  @Override
  public boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return statementContext;
  }

  @Override
  public void endFile() {
    // Intentionally empty
  }
}

class InterruptingCodeConsumer extends StubCodeConsumer {
  @Override
  public boolean continueProcessing() {
    return false;
  }
}