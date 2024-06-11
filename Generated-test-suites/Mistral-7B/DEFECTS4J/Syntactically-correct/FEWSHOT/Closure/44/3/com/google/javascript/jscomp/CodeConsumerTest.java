package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestCodeConsumer extends CodeConsumer {
    char lastChar;

    abstract char getLastChar();

    @Override
    char getLastChar() {
      return lastChar;
    }
  }

  @Test
  public void testBaseClass() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.statementNeedsEnded);
    assertEquals(false, consumer.statementStarted);
    assertFalse(consumer.sawFunction);

    consumer.beginBlock();
    consumer.append("{");
    consumer.appendBlockStart();
    consumer.appendBlockEnd();
    consumer.endBlock();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        lastChar = 'a';
      }
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        lastChar = 'b';
      }
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertEquals('b', consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        lastChar = 'c';
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        lastChar = str.charAt(str.length() - 1);
      }
    };

    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.appendBlockStart();

    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.appendBlockStart();
    consumer.appendBlockEnd();

    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.startNewLine();

    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.maybeLineBreak();

    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.maybeCutLine();

    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.endLine();

    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.notePreferredLineBreak();

    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginBlock();

    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginBlock();
    consumer.appendBlockEnd();

    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginBlock();
    consumer.endBlock(true);

    assertEquals("\n}", consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.listSeparator();

    assertEquals(",", consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.endStatement();

    assertEquals(";", consumer.getLastChar());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.endStatement(true);

    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.maybeEndStatement();

    assertEquals(";", consumer.getLastChar());
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testMaybeEndStatementWithLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.maybeEndStatement(true);

    assertEquals("\n", consumer.getLastChar());
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();

    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndFunctionWithLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction(true);

    assertEquals("\n}", consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginCaseBody();

    assertEquals(":", consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.beginCaseBody();
    consumer.endCaseBody();

    assertEquals("", consumer.getLastChar());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.add("def");

    assertEquals("def", consumer.getLastChar());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.addOp("+", false);
    consumer.add("def");

    assertEquals("+def", consumer.getLastChar());
  }

  @Test
  public void testAddOpBinary() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.addOp("+", true);
    consumer.add("def");

    assertEquals("abc + def", consumer.getLastChar());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.addNumber(1);
    consumer.addNumber(2);
    consumer.addNumber(3.0);

    assertEquals("123.0", consumer.getLastChar());
  }

  @Test
  public void testIsNegativeZero() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.isNegativeZero(0.0));
    assertEquals(true, consumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));

    assertFalse(consumer.isWordChar(','));
    assertFalse(consumer.isWordChar('('));
    assertFalse(consumer.isWordChar(')'));
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

    assertTrue(consumer.breakAfterBlockFor(null, false));
    assertFalse(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");
    consumer.endFile();

    assertEquals("\n", consumer.getLastChar());
  }
}