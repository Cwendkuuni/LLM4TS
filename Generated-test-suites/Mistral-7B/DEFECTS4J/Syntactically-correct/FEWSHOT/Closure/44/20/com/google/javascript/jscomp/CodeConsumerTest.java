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
    assertTrue(consumer.statementStarted);
    assertFalse(sawFunction);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
    assertFalse(sawFunction);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
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
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.toString());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeLineBreak();
    assertEquals("foo\n", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foofoo");
    consumer.maybeCutLine();
    assertEquals("foo\nfoo", consumer.toString());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endLine();
    assertEquals("foo\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    assertEquals("foo{\nbar\n}\n", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.listSeparator();
    consumer.append("bar");
    assertEquals("foo,\nbar", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endStatement();
    consumer.append("bar");
    assertEquals("foo;\nbar", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    assertEquals("foo;\nbar", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.beginBlock();
    consumer.endFunction();
    consumer.append("bar");
    consumer.endBlock();
    assertEquals("foo{\n}\nbar", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo:");
    consumer.beginCaseBody();
    consumer.append("bar");
    consumer.endCaseBody();
    assertEquals("foo:\nbar\n", consumer.toString());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foo bar", consumer.toString());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.addOp("+", true);
    consumer.append("bar");
    assertEquals("foo + bar", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.addNumber(1);
    consumer.addNumber(0.5);
    assertEquals("foo 1.5", consumer.toString());
  }

  @Test
  public void testIsNegativeZero() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar('-'));
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
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }
}