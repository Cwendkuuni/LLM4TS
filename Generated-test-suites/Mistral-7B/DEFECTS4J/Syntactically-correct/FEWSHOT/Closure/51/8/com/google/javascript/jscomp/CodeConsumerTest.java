package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('!'));
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    assertEquals("Hello", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendOp("+", false);
    consumer.add("1");
    consumer.appendOp("+", false);
    consumer.add("2");
    assertEquals("1 + 2", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    consumer.add("1");
    consumer.addOp("+", false);
    consumer.add("2");
    assertEquals("1 + 2", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.0);
    assertEquals("1", consumer.getCode());
    consumer.addNumber(-1.0);
    assertEquals("-1", consumer.getCode());
    consumer.addNumber(-1.2345);
    assertEquals("-1.2345", consumer.getCode());
    consumer.addNumber(1234567890.12345);
    assertEquals("1234567890.12345", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("Hello");
    consumer.endBlock();
    assertEquals("{Hello}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("Hello");
    consumer.endBlock(true);
    assertEquals("{\nHello\n}", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.maybeLineBreak();
    consumer.append("World");
    assertEquals("Hello\nWorld", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.maybeCutLine();
    consumer.append("World");
    assertEquals("Hello\nWorld", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.endLine();
    consumer.append("World");
    assertEquals("Hello\nWorld", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.notePreferredLineBreak();
    consumer.append("World");
    assertEquals("Hello\nWorld", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("Hello");
    consumer.endCaseBody();
    assertEquals(":\nHello\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public
	void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(null, false));
    assertFalse(consumer.breakAfterBlockFor(null, true));
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return 'x';
  }

  @Override
  String getCode() {
    String code = super.getCode();
    // Reset the last character for next test.
    getLastChar();
    return code;
  }
}