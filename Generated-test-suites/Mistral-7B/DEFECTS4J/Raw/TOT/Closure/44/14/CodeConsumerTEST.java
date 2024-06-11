package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newText("test");
    consumer.startSourceMapping(node);
    String expected = "//# sourceMappingURL=test.js.map";
    String actual = consumer.getCode();
    assertTrue(actual.startsWith(expected));
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newText("test");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    String actual = consumer.getCode();
    String expected = "//# sourceMappingURL=test.js.map";
    String expectedCode = "test";
    String actualCode = consumer.getCodeAfterLastChar();
    assertTrue(actual.startsWith(expected));
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.setContinueProcessing(false);
    boolean result = consumer.continueProcessing();
    assertFalse(result);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    String actual = consumer.getCode();
    String expected = "test";
    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    String actual = consumer.getCode();
    String expected = "\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    String actual = consumer.getCode();
    String expected = "test\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    char actual = consumer.getLastChar();
    char expected = 't';
    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    String actual = consumer.getCode();
    String expected = "test";
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String actual = consumer.getCode();
    String expected = "{\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String actual = consumer.getCode();
    String expected = "{\n test\n}";
    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String actual = consumer.getCode();
    String expected = "test1,\n test2\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.endStatement();
    String actual = consumer.getCode();
    String expected = "test1;\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.append(";");
    consumer.maybeEndStatement();
    String actual = consumer.getCode();
    String expected = "test1;\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function testFunction() {}\n");
    consumer.endFunction();
    String actual = consumer.getCode();
    String expected = "{\n function testFunction() {}\n}\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    String actual = consumer.getCode();
    String expected = "{\n:\n}";
    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String actual = consumer.getCode();
    String expected = "{\n :\n test\n}";
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("5");
    consumer.addOp("+", true);
    consumer.append("3");
    String actual = consumer.getCode();
    String expected = "5 + 3\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(5.5);
    String actual = consumer.getCode();
    String expected = "5.5\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newLiteral(true);
    boolean result = consumer.shouldPreserveExtraBlocks(node, true);
    assertTrue(result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = NodeOperations.binaryOp("+", NodeOperations.literal(5), NodeOperations.literal(3));
    boolean result = consumer.breakAfterBlockFor(node, true);
    assertFalse(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String actual = consumer.getCode();
    assertTrue(actual.isEmpty());
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder codeBuilder = new StringBuilder();
  private StringBuilder codeBuilderAfterLastChar = new StringBuilder();

  @Override
  void startSourceMapping(Node node) {
    super.startSourceMapping(node);
    codeBuilder.append("//# sourceMappingURL=");
    codeBuilder.append(node.getSourceName());
    codeBuilder.append(".js.map");
  }

  @Override
  void endSourceMapping(Node node) {
    super.endSourceMapping(node);
    codeBuilderAfterLastChar.append(getLastChar());
  }

  @Override
  char getLastChar() {
    return codeBuilderAfterLastChar.charAt(0);
  }

  @Override
  void append(String str) {
    super.append(str);
    codeBuilder.append(str);
  }

  @Override
  void appendOp(String op, boolean binOp) {
    super.appendOp(op, binOp);
    codeBuilderAfterLastChar.append(op.charAt(0));
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
    codeBuilder.append(x);
  }

  @Override
  String getCode() {
    String result = codeBuilder.toString();
    codeBuilder.setLength(0);
    return result;
  }

  @Override
  String getCodeAfterLastChar() {
    String result = codeBuilderAfterLastChar.toString();
    codeBuilderAfterLastChar.setLength(0);
    return result;
  }

  @Override
  boolean continueProcessing() {
    return super.continueProcessing();
  }

  @Override
  void beginBlock() {
    super.beginBlock();
    codeBuilder.append("{\n");
  }

  @Override
  void endBlock() {
    super.endBlock();
    codeBuilder.append("}");
  }

  @Override
  void listSeparator() {
    super.listSeparator();
    codeBuilder.append(",\n");
  }

  @Override
  void endLine() {
    super.endLine();
    codeBuilder.append("\n");
  }

  @Override
  void maybeCutLine() {
    super.maybeCutLine();
    codeBuilder.append("\n");
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
    codeBuilder.append("\n");
  }

  @Override
  void beginCaseBody() {
    super.beginCaseBody();
    codeBuilder.append(":\n");
  }

  @Override
  void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
    codeBuilder.append(newcode);
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return true;
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return statementContext;
  }

  @Override
  void endFile() {
    super.endFile();
  }
}