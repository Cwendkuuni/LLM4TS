package com.google.javascript.jscomp; // for imports

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for CodeConsumer.
 */
public class CodeConsumerTest {
  CodeConsumer consumer; // test subject

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer(); // use a test subclass for testing
  }

  

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    assertNull(consumer.getLastChar());

    consumer.add("hello");
    assertEquals('h', consumer.getLastChar());

    consumer.add("world");
    assertEquals('r', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    consumer.append(" world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("hello");
    consumer.appendBlockEnd();
    assertEquals("{\nhello}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("hello");
    consumer.startNewLine();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("hello");
    consumer.maybeLineBreak();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("hello");
    consumer.maybeCutLine();
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("hello");
    consumer.endLine();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("hello");
    consumer.notePreferredLineBreak();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.append("hello");
    consumer.beginBlock();
    consumer.append("world");
    consumer.endBlock();
    assertEquals("hello{\nworld}\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function test() {\n");
    consumer.endFunction();
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("function test() {\n");
    consumer.endFunction(true);
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("case 0:\n");
    consumer.beginCaseBody();
    consumer.append("break;\n");
    consumer.endCaseBody();
    assertEquals("case 0:\n{\nbreak;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.add("hello");
    consumer.add(" world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(123.456);
    assertEquals("123.456", consumer.getCode());

    consumer.addNumber(-123.456);
    assertEquals("-123.456", consumer.getCode());

    consumer.addNumber(0.0);
    assertEquals("0", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(-0.0));
    assertEquals(true, CodeConsumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(Character.isLetterOrDigit('a'));

    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test(expected = AssertionError.class)
  public void testShouldPreserveExtraBlocksThrowsException() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    node.setType(Node.AST_BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  

  static class TestCodeConsumer extends CodeConsumer {
    @Override
    void add(String newcode) {
      super.add(newcode);
      this.code += newcode;
    }

    String getCode() {
      return code;
    }
  }
}