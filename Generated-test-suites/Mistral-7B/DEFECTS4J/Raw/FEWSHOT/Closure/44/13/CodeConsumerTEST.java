package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    char lastChar;

    @SuppressWarnings("unchecked")
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
    assertEquals(node, consumer.getCurrentNode());
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getCurrentNode());
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    TestCodeConsumer consumer = new TestConsumerFalse() {
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
    consumer.append("foo");
    consumer.maybeCutLine();
    assertEquals("foo\n", consumer.toString());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.toString());
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
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();
    assertEquals("{\nfoo}\n", consumer.toString());
  }

  @Test
  public void testEndBlockWithSemicolon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock(true);
    assertEquals("{\nfoo;\n}", consumer.toString());
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
    assertEquals("foo;\n", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    assertEquals("foo;\n", consumer.toString());
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
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {\n}\n}", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() { return 42; }");
    consumer.endFunction(true);
    assertEquals("{\nfunction foo() {\n  return 42;\n}\n}", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case 42:");
    consumer.beginCaseBody();
    consumer.append("return 42;");
    consumer.endCaseBody();
    assertEquals("case 42:\n{\n  return 42;\n}", consumer.toString());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foobar", consumer.toString());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    consumer.addOp("+", false);
    consumer.append("bar");
    assertEquals("foo+bar", consumer.toString());
  }

  @Test
  public void testAddOpBinary() {
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
    consumer.addNumber(42.0);
    assertEquals("foonum42", consumer.toString());
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
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('1'));
    assertFalse(consumer.isWordChar(' '));
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
  public void testBreakAfterBlockForTrue() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }

  private static class TestConsumerFalse extends TestCodeConsumer {
  }
}