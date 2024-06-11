package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract static class TestCodeConsumer extends CodeConsumer {
    @Override
    abstract char getLastChar();
  }

  static class TestUtil extends TestCodeConsumer {

    @Override
    char getLastChar() {
      return '\u0000';
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestUtil consumer = new TestUtil();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Nothing to assert here
  }

  @Test
  public void testEndSourceMapping() {
    TestUtil consumer = new TestUtil();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Nothing to assert here
  }

  @Test
  public void testContinueProcessing() {
    TestUtil consumer = new TestUtil();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    char expected = 't';
    char actual = consumer.getLastChar();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    TestUtil consumer = new TestUtil();
    consumer.addIdentifier("identifier");
    consumer.append(" = ");
    consumer.addIdentifier("value");
    consumer.append(";");

    String expected = "identifier = value;\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");

    String expected = "test";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockStart() {
    TestUtil consumer = new TestUtil();
    consumer.beginBlock();

    String expected = "{\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    TestUtil consumer = new TestUtil();
    consumer.appendBlockStart();
    consumer.append("test");
    consumer.appendBlockEnd();

    String expected = "{\ntest\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.startNewLine();

    String expected = "test\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.maybeLineBreak();

    String expected = "test\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeCutLine() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.maybeCutLine();

    String expected = "test\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndLine() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.endLine();

    String expected = "test\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestUtil consumer = new TestUtil();
    consumer.notePreferredLineBreak();

    String expected = "\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.beginBlock();

    String expected = "test{\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("test2");
    consumer.endBlock();

    String expected = "test{\ntest2\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlockWithLineBreak() {
    TestUtil consumer = new TestUtil();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("test2");
    consumer.endBlock(true);

    String expected = "test{\ntest2\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    TestUtil consumer = new TestUtil();
    consumer.append("[1, 2, ");
    consumer.listSeparator();
    consumer.append("3, 4]");

    String expected = "[1, 2, 3, 4]";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    TestUtil consumer = new TestUtil();
    consumer.append("if (x > 0) {");
    consumer.endStatement();

    String expected = "if (x > 0) {\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestUtil consumer = new TestUtil();
    consumer.append("if (x > 0) {");
    consumer.endStatement(true);

    String expected = "if (x > 0) {\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    TestUtil consumer = new TestUtil();
    consumer.append("if (x > 0) {");
    consumer.append("return x;");
    consumer.maybeEndStatement();

    String expected = "if (x > 0) {\nreturn x;\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    TestUtil consumer = new TestUtil();
    consumer.append("function test() {");
    consumer.endFunction();

    String expected = "function test() {\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestUtil consumer = new TestUtil();
    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endFunction(true);

    String expected = "function test() {\nreturn 1;\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    TestUtil consumer = new TestUtil();
    consumer.append("switch (x) {\ncase 1:");
    consumer.beginCaseBody();

    String expected = "switch (x) {\ncase 1:\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    TestUtil consumer = new TestUtil();
    consumer.append("switch (x) {\ncase 1:\n");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();

    String expected = "switch (x) {\ncase 1:\nreturn 1;\n}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    TestUtil consumer = new TestUtil();
    consumer.append("int x = ");
    consumer.add("1");
    consumer.append(" + ");
   consumer.add("2");
    consumer.append(";");

    String expected = "int x = 1 + 2;\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    TestUtil consumer = new TestUtil();
    consumer.append("int x = ");
    consumer.addOp("1 + ", true);
    consumer.add("2");
    consumer.append(";");

    String expected = "int x = 1 + 2;\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    TestUtil consumer = new TestUtil();
    consumer.append("int x = ");
    consumer.addNumber(1.1);
    consumer.append(";");

    String expected = "int x = 1.1;\n";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('1'));
    assertFalse(isWordChar('.'));
    assertFalse(isWordChar('-'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestUtil consumer = new TestUtil();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    TestUtil consumer = new TestUtil();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    TestUtil consumer = new TestUtil();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testToString() {
    TestUtil consumer = new TestUtil();
    String expected = "";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }
}