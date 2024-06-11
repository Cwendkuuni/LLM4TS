package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('9'));

    assertFalse(CodeConsumer.isWordChar(' '));
    assertFalse(CodeConsumer.isWordChar('!'));
    assertFalse(CodeConsumer.isWordChar('@'));
  }

  @Test
  public void testStartSourceMapping() {
    // Abstract method, can't test directly
  }

  @Test
  public void testEndSourceMapping() {
    // Abstract method, can't test directly
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingWhenFalse() {
    CodeConsumer consumer = new TestCodeConsumerFalse();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppendIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    consumer.maybeEndStatement();
    consumer.append(" = ");
    consumer.addIdentifier("value");
    consumer.maybeEndStatement();
    assertEquals("identifier value", consumer.toString());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo bar", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    consumer.maybeEndStatement();
    assertEquals("foo {\nbar\n}", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    consumer.maybeEndStatement();
    assertEquals("foo {\nbar\n}", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.startNewLine();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo\nbar", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeLineBreak();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo\nbar", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.append("bar");
    consumer.maybeCutLine();
    consumer.append("baz");
    consumer.maybeEndStatement();
    assertEquals("foo\nbaz", consumer.toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endLine();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo\nbar", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.notePreferredLineBreak();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo\nbar", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    consumer.maybeEndStatement();
    assertEquals("foo{\nbart\n}", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    consumer.maybeEndStatement();
    assertEquals("foo{\nbart\n}", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("[");
    consumer.listSeparator();
    consumer.append("1");
    consumer.listSeparator();
    consumer.append("2");
    consumer.endLine();
    consumer.maybeEndStatement();
    assertEquals("[1,\n2\n]", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endStatement();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo;\nbar", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo;\nbar", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foobar", consumer.toString());
  }

  @Test
  public void testMaybeEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement(true);
    consumer.append("bar");
    consumer.maybeEndStatement();
    assertEquals("foo;\nfoobar", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endCaseBody();
    consumer.maybeEndStatement();
    assertEquals("case 1:\nbreak;\n", consumer.toString());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endCaseBody();
    consumer.maybeEndStatement();
    assertEquals("case 1:\nbreak;\n", consumer.toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.add("bar");
    consumer.maybeEndStatement();
    assertEquals("foobar", consumer.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1");
    consumer.appendOp("+", true);
    consumer.append("2");
    consumer.maybeEndStatement();
    assertEquals("1 + 2", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1.23456");
    consumer.addNumber(2.71828);
    consumer.maybeEndStatement();
    assertEquals("1.23456 2.71828", consumer.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return '\0';
  }

  @Override
  void toString() {
    StringBuilder sb = new StringBuilder();
    super.toString(sb);
    return sb.toString();
  }
}

class TestCodeConsumerFalse extends CodeConsumer {
  @Override
  boolean continueProcessing() {
    return false;
  }
}