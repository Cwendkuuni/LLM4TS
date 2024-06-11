package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumerTest;

public class CodeConsumerTest extends CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createNode();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
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
    String code = "function foo() {}";
    consumer.add(code);
    char lastChar = consumer.getLastChar();
    assertTrue(lastChar == '}');
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { return 42; }");
    String expectedCode = "function foo() { return 42; }";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = createTestConsumer();
    consumer.appendBlockStart();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = createTestConsumer();
    consumer.appendBlockEnd();
    String expectedCode = "}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.startNewLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = createTestConsumer();
    consumer.maybeLineBreak();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = createTestConsumer();
    consumer.endLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = createTestConsumer();
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = createTestConsumer();
    consumer.endBlock();
    String expectedCode = "}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("return 42;");
    consumer.endBlock(true);
    String expectedCode = "{\nreturn 42;\n}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = createTestConsumer();
    consumer.listSeparator();
    String expectedCode = ", ";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { }");
    consumer.endStatement();
    String expectedCode = "function foo() {\n};\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { }");
    consumer.endStatement(true);
    String expectedCode = "function foo() {\n};\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { }");
    consumer.maybeEndStatement();
    String expectedCode = "function foo() {\n};\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { }");
    consumer.endFunction();
    String expectedCode = "function foo() {\n}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginBlock();
    consumer.append("function foo() { }");
    consumer.endFunction(true);
    String expectedCode = "{\nfunction foo() {\n}\n}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = createTestConsumer();
    consumer.beginCaseBody();
    String expectedCode = ": \n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = createTestConsumer();
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = createTestConsumer();
    consumer.add("function foo() { }");
    String expectedCode = "function foo() { }\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = createTestConsumer();
    consumer.addOp("+", true);
    String expectedCode = "+";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = createTestConsumer();
    consumer.addNumber(42.0);
    String expectedCode = "42.0";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('F'));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = createTestConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createNode();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = createTestConsumer();
    Node node = createNode();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = createTestConsumer();
    consumer.endFile();
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = createTestConsumer();
    consumer.append("function foo() { }");
    consumer.endFunction();
    assertTrue(consumer.sawFunction());
  }
}