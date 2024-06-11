package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('F'));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test(expected = AssertionError.class)
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertTrue(consumer.getLastChar() == '{');
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertTrue(consumer.getLastChar() == '}');
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertTrue(consumer.getLastChar() == ':');
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertTrue(consumer.getCode().isEmpty());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("foo");
    consumer.add("bar");
    consumer.add("baz");
    assertEquals("foobarbaz", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    consumer.add("1");
    consumer.addOp("+", false);
    consumer.add("2");
    assertEquals("1+1+2", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.2);
    assertEquals("1.2", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertTrue(consumer.getCode().isEmpty());
  }

  private String getCode() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    return consumer.getCode();
  }

  private char getLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    return consumer.getLastChar();
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void append(String s) {
      code.append(s);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }
}