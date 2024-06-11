package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
    assertFalse(consumer.isNegativeZero(-1.0));
    assertFalse(consumer.isNegativeZero(1.2));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar('!'));
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Test method does not require further assertions.
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Test method does not require further assertions.
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
    // Test method does not require further assertions.
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
    consumer.append("bar");
    assertEquals('r', consumer.getLastChar());
    // Test methods do not require further assertions for other test cases.
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.getLastCode());
    consumer.append(" bar");
    assertEquals("foo bar", consumer.getLastCode());
    // Test methods do not require further assertions for other test cases.
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeLineBreak();
    assertEquals("\nfoo", consumer.getLastCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeCutLine();
    assertEquals("foo", consumer.getLastCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endLine();
    assertEquals("\nfoo", consumer.getLastCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getLastCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("return 42;");
    consumer.endBlock(true);
    assertEquals("}\nreturn 42;\n", consumer.getLastCode());
  }

  @Test  // Test for listSeparator() method
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.listSeparator();
    assertEquals("foo,\n", consumer.getLastCode());
  }

  @Test // Test for endStatement() method
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (x < 42) {");
    consumer.endStatement();
    assertEquals("if (x < 42) {\n", consumer.getLastCode());
    consumer.append("x++;");
    consumer.endStatement();
    assertEquals("if (x < 42) {\n  x++;\n}", consumer.getLastCode());
    consumer.append("x++;");
    consumer.endStatement(true);
    assertEquals("if (x < 42) {\n  x++;\n}\n", consumer.getLastCode());
  }

  @Test // Test for endFunction() method
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getLastCode());
  }

  @Test // Test for beginCaseBody() method
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    assertEquals("switch (x) {\n  case:\n", consumer.getLastCode());
  }

  @Test // Test for endCaseBody() method
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case:\n    break;\n}", consumer.getLastCode());
  }

  @Test // Test for add() method
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.add("int y = 43;");
    assertEquals("int x = 42;\nint y = 43;\n", consumer.getLastCode());
  }

  @Test // Test for addOp() method
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.addOp("+", true);
    consumer.add(Integer.toString(5));
    assertEquals("int x = 42;\nint x = x + 5;\n", consumer.getLastCode());
  }

  @Test // Test for addNumber() method
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 0;");
    consumer.addNumber(1.5);
    assertEquals("int x = 0;\nint x = 1;\n", consumer.getLastCode());
  }

  @Test // Test for shouldPreserveExtraBlocks() method
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
    // Test method does not require further assertions.
  }

  @Test // Test for breakAfterBlockFor() method
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    // Test method does not require further assertions.
  }

  @Test // Test for endFile() method
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // Test method does not require further assertions.
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastCode().charAt(0);
  }

  @Override
  public String getLastCode() {
    return lastCode;
  }

  public String lastCode = "";
}