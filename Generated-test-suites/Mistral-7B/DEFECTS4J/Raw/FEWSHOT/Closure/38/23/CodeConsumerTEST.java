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
    consumer.append("foo");
    consumer.lastChar = 'o';
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    consumer.addIdentifier("anotherIdentifier");
    consumer.maybeEndStatement();
    consumer.append(";");
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.appendBlockEnd();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndBlockWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock(true);
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    consumer.listSeparator();
    consumer.addIdentifier("bar");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    consumer.endStatement();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    consumer.endStatement(true);
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.addIdentifier("functionName");
    consumer.endFunction();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case ");
    consumer.beginCaseBody();
    consumer.append(":");
    consumer.endLine();
    consumer.maybeEndStatement();
    consumer.append("break;");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case ");
    consumer.beginCaseBody();
    consumer.append(":");
    consumer.endLine();
    consumer.maybeEndStatement();
    consumer.append("break;");
    consumer.maybeEndStatement();
    consumer.endCaseBody();
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("foo");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("5");
    consumer.addOp("+", false);
    consumer.add("2");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(5.5);
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
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
    assertTrue(consumer.isWordChar('1'));
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
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
  }

  @Test
  public void testSawFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.addIdentifier("functionName");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}