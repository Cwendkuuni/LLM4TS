package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Testing the effect of startSourceMapping cannot be done without a derived class
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Testing the effect of endSourceMapping cannot be tested without a derived class
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("foo");
    // Testing the effect of addIdentifier cannot be done without a derived class
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    String result = consumer.getResult();
    assertEquals("foo", result);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String result = consumer.getResult();
    assertEquals("{\n", result);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String result = consumer.getResult();
    assertEquals("}\n", result);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String result = consumer.getResult();
    assertEquals("\n", result);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    String result = consumer.getResult();
    assertTrue(result.contains("\n"));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // Testing the effect of maybeCutLine cannot be done without a derived class
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String result = consumer.getResult();
    assertEquals("\n", result);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Testing the effect of notePreferredLineBreak cannot be done without a derived class
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String result = consumer.getResult();
    assertEquals("{\n", result);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    String result = consumer.getResult();
    assertEquals("}\n", result);
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    String result = consumer.getResult();
    assertEquals("}\n", result);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    String result = consumer.getResult();
    assertEquals(",\n", result);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
   String result = consumer.getResult();
    assertTrue(result.contains(";"));
  }

  @Test
  public void testEndStatementWithEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    String result = consumer.getResult();
    assertTrue(result.contains(";\n"));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    String result = consumer.getResult();
    assertTrue(result.contains(";"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    String result = consumer.getResult();
    assertTrue(result.contains("}"));
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    String result = consumer.getResult();
    assertTrue(result.contains("}"));
    assertTrue(result.contains("\n"));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    String result = consumer.getResult();
    assertEquals(":\n", result);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    // Testing the effect of endCaseBody cannot be done without a derived class
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("foo");
    String result = consumer.getResult();
    assertEquals("foo", result);
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendOp("+", false);
    String result = consumer.getResult();
    assertEquals("+", result);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", false);
    String result = consumer.getResult();
    assertEquals(" +", result);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(42.0);
    String result = consumer.getResult();
    assertEquals("42.0\n", result);
  }

  @Test
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // Testing the effect of endFile cannot be done without a derived class
  }

}

class StubCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return 'x';
  }

  @Override
  String getResult() {
    return result;
  }

  String result = "";
}