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
    // Nothing to assert here
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Nothing to assert here
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.lastChar = 'o';
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    // Nothing to assert here
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    // Nothing to assert here
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    // Nothing to assert here
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    // Nothing to assert here
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    // Nothing to assert here
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // Nothing to assert here
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.listSeparator();
    assertEquals(',', consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    // Nothing to assert here
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    // Nothing to assert here
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {}");
    consumer.endFunction();
    assertFalse(consumer.sawFunction);
  }

  @Test
  public void testEndFunctionWithSemicolon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {}");
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    // Nothing to assert here
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    // Nothing to assert here
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", false);
    assertEquals('+', consumer.getLastChar());
  }

  @Test
  public void testAddOpBinary() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    assertEquals(' ', consumer.getLastChar());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(1.0);
    assertEquals('.', consumer.getLastChar());
  }

  @Test
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar('.'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    // Nothing to assert here
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // Nothing to assert here
  }
}