package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestingCodeConsumer extends CodeConsumer {
    char lastChar;

    @Override
    char getLastChar() {
      return lastChar;
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMapping() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    TestingCodeConsumer consumer = new TestingCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.lastChar = 'a';
    assertEquals('a', consumer.getLastChar());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAbstractMethods() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.append(null);
  }

  @Test
  public void testAddIdentifier() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.addIdentifier("foo");
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testAppend() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    String str = "bar";
    consumer.append(str);
    assertEquals(str, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.maybeLineBreak();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeCutLine() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.notePreferredLineBreak();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testBeginBlock() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();
    assertEquals("{\nfoo\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithContext() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("foo");
    consumer.endCaseBody();
    assertEquals(":\nfoo\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.add("foo");
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.addOp("++", false);
    assertEquals("++\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.addNumber(10);
    assertEquals("10\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestingCodeConsumer consumer = new TestingCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    Node node1 = new Node();
    Node node2 = new Node();
    assertTrue(consumer.breakAfterBlockFor(node1, false));
    assertFalse(consumer.breakAfterBlockFor(node2, false));
    assertTrue(consumer.breakAfterBlockFor(node1, true));
  }

  @Test
  public void testEndFile() {
    TestingCodeConsumer consumer = new TestingCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}