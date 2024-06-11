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
    // no assertions here, just testing method call
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // no assertions here, just testing method calls
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessing_false() {
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
    consumer.add("foo");
    consumer.lastChar = 'o';
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAppend_empty() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("");
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode().substring(consumer.getCode().length() - 1));
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    // no assertions here, just testing method call
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    // no assertions here, just testing method call
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode().substring(consumer.getCode().length() - 1));
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // no assertions here, just testing method call
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlock_shouldEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode().substring(consumer.getCode().length() - 2));
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
    // no assertions here, just testing method call
  }

  @Test
  public void testEndStatement_needSemiColon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode().substring(consumer.getCode().length() - 2));
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeEndStatement();
    // no assertions here, just testing method call
  }

  @Test
  public void testMaybeEndStatement_statementNeedsEnded() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.statementNeedsEnded = true;
    consumer.maybeEndStatement();
    assertEquals(";\n", consumer.getCode().substring(consumer.getCode().length() - 2));
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode().substring(consumer.getCode().length() - 2));
  }

  @Test
  public void testEndFunction_statementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    // no assertions here, just testing method call
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAdd_empty() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("");
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.appendOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals(" +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1);
    assertEquals("1", consumer.getCode());
  }

  @Test
  public void testAddNumber_negative() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(-1);
    assertEquals("-1", consumer.getCode());
  }

  @Test
  public void testAddNumber_long() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(12345);
    assertEquals("12345", consumer.getCode());
  }

  @Test
  public void testAddNumber_double() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.23);
    assertEquals("1.23", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0));
    assertFalse(consumer.isNegativeZero(1));
    assertFalse(consumer.isNegativeZero(-1));
    assertFalse(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
    assertFalse(consumer.isNegativeZero(-1.0));
    assertFalse(consumer.isNegativeZero(Double.NEGATIVE_INFINITY));
    assertFalse(consumer.isNegativeZero(Double.POSITIVE_INFINITY));
    assertFalse(consumer.isNegativeZero(Double.NaN));
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('z'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
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
  }

  @Test
  public void testBreakAfterBlockFor_statementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // no assertions here, just testing method call
  }
}