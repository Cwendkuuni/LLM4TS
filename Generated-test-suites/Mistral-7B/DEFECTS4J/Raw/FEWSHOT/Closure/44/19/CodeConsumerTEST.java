package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestCodeConsumer extends CodeConsumer {
    TestCodeConsumer() {
      startSourceMapping(mock(Node.class));
      endSourceMapping(mock(Node.class));
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    assertTrue(consumer.statementNeedsEnded);
    assertTrue(consumer.statementStarted);
    assertFalse(consumer.sawFunction);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        // Do nothing
      }
    };
    consumer.addIdentifier("test");
    assertEquals("test", consumer.getLastCode());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        // Do nothing
      }
    };
    consumer.append("test");
    assertEquals("test", consumer.getLastCode());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getLastCode());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    // The method does not append any code, so the last code is the same as before
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    // The method does not append any code, so the last code is the same as before
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // The method does not append any code, so the last code is the same as before
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getLastCode());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getLastCode());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.getLastCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getLastCode());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getLastCode());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.getLastCode());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.getLastCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1);
    assertEquals("1", consumer.getLastCode());

    consumer.addNumber(1.5);
    assertEquals("1.5", consumer.getLastCode());

    consumer.addNumber(-1.5);
    assertEquals("-1.5", consumer.getLastCode());

    consumer.addNumber(-0.0);
    assertEquals("-0.0", consumer.getLastCode());

    consumer.addNumber(Integer.MIN_VALUE);
    assertEquals("-2147483648", consumer.getLastCode());

    consumer.addNumber(Long.MAX_VALUE);
    assertEquals("9223372036854775807", consumer.getLastCode());
  }

  @Test
  public void testIsNegativeZero() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));

    double negZero = -0.0;
    assertTrue(consumer.isNegativeZero(negZero));

    assertFalse(consumer.isNegativeZero(Double.NEGATIVE_INFINITY));
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));

    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('('));
    assertFalse(consumer.isWordChar(')'));
    assertFalse(consumer.isWordChar('+'));
    assertFalse(consumer.isWordChar('-'));
    assertFalse(consumer.isWordChar('/'));
    assertFalse(consumer.isWordChar('\\'));
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
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getLastCode());
  }
}