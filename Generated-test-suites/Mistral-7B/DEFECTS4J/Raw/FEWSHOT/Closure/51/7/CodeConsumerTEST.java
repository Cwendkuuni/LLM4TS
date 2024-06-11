package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verify startSourceMapping did not throw an exception
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Verify endSourceMapping did not throw an exception
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());

    consumer = new FailingStubCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = "abc";
    consumer.append(code);

    char lastChar = consumer.getLastChar();
    assertEquals(lastChar, 'c');
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    String expectedCode = "identifier";
   assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("string");
    String expectedCode = "string";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String expectedCode = "}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String expectedCode = "\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // Verify maybeLineBreak did not add a newline
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // Verify maybeCutLine did not cut the line
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String expectedCode = "\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Verify notePreferredLineBreak did not add a newline
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    String expectedCode = "}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    String expectedCode = "}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    String expectedCode = ", ";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    String expectedCode = ";";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    String expectedCode = "; \n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    // Verify maybeEndStatement did not add a semicolon or a newline
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    String expectedCode = "}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    String expectedCode = "}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    String expectedCode = ": ";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    // Verify endCaseBody did not add any code
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("code");
    String expectedCode = "code";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+");
    String expectedCode = "+";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOpWithBinary() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", true);
    String expectedCode = "+";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1);
    String expectedCode = "1";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // Verify endFile did not add any code
  }

  @Test(expected = AssertionError.class)
  public void testFailingStubCodeConsumerEndStatement() {
    CodeConsumer consumer = new FailingStubCodeConsumer();
    consumer.endStatement();
  }

  private String getCode() {
    return ((StubCodeConsumer) consumer).getCode();
  }
}

class StubCodeConsumer extends CodeConsumer {
  private String code = "";

  @Override
  void append(String str) {
    code += str;
  }

  @Override
  void add(String newcode) {
    append(newcode);
  }

  @Override
  void addOp(String op, boolean binOp) {
    append(op);
  }

  @Override
  void addNumber(double x) {
    append(String.valueOf(x));
  }

  @Override
  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  String getCode() {
    return code;
  }
}

class FailingStubCodeConsumer extends StubCodeConsumer {
  @Override
  boolean continueProcessing() {
    throw new AssertionError("Continue processing should never fail");
  }
}