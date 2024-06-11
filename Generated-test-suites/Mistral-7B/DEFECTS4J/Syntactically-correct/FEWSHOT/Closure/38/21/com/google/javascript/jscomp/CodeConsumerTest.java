package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    char lastChar;

    @Override
    char getLastChar() {
      return lastChar;
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.lastChar = 'A';
    assertEquals('A', consumer.getLastChar());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAbstractMethods() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}", "\n", consumer.getLastChars(2));
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",", consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndStatementWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);
  }

  @Test
  public void testEndFunctionWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":", consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("foo");
    assertEquals("foo", consumer.getLastCode());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastCode());
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendOp("+", false);
    assertEquals("+", consumer.getLastChar());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("==", false);
    assertEquals("==", consumer.getLastChar());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(123);
    assertEquals("123", consumer.getLastCode());
  }

  @Test
  public void testIsNegativeZero() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertFalse(consumer.statementNeedsEnded);
  }
}