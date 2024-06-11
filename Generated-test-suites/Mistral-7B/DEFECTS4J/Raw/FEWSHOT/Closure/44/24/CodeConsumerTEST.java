package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  private static class TestConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void append(String str) {
      System.out.print(str);
    }

    @Override
    void add(String newcode) {
      append(newcode);
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestConsumer consumer = new TestConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestConsumer consumer = new TestConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    TestConsumer consumer = new TestConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    Character expected = 'f';
    assertEquals(expected, consumer.getLastChar());

    consumer.append("bar");
    expected = 'r';
    assertEquals(expected, consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    TestConsumer consumer = new TestConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    TestConsumer consumer = new TestConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    TestConsumer consumer = new TestConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    TestConsumer consumer = new TestConsumer();
    consumer.maybeLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    TestConsumer consumer = new TestConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    TestConsumer consumer = new TestConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestConsumer consumer = new TestConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();
    assertEquals("{\nfoo\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock(true);
    assertEquals("{\nfoo\n}", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    TestConsumer consumer = new TestConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    assertEquals("foo;\n", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginBlock();
    consumer.append("function foo() { return 42; }");
    consumer.endFunction(true);
    assertEquals("{\nfunction foo() { return 42; }\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    TestConsumer consumer = new TestConsumer();
    consumer.beginCaseBody();
    consumer.append("foo");
    consumer.endCaseBody();
    assertEquals(":\nfoo\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foo bar", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    TestConsumer consumer = new TestConsumer();
    consumer.addIdentifier("FOO");
    assertEquals("FOO", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.addOp("+", true);
    consumer.append("bar");
    assertEquals("foo + bar", consumer.getCode());
  }

  @Test
  public void testAddDouble() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.addNumber(42.0);
    assertEquals("foo 42.0", consumer.getCode());
  }

  @Test
  public void testAddNumberWithNegativeSign() {
    TestConsumer consumer = new TestConsumer();
    consumer.append("foo");
    consumer.addNumber(-42.0);
    assertEquals("foo -42.0", consumer.getCode());
  }
}