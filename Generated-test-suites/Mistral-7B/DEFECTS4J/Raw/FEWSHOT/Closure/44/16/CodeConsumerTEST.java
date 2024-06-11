package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void append(String str) {
      System.out.print(str);
    }

    @Override
    void add(String newcode) {
      super.add(newcode);
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    char lastChar = consumer.getLastChar();
    assertEquals("c", lastChar);
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.append("def");
    assertEquals("abcdef", consumer.getCode());
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
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
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
  public void testEndBlockWithNewline() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) ");
    consumer.endStatement();
    assertEquals("if (true) {\n}", consumer.getCode());
    consumer.append("}");
    consumer.endStatement();
    assertEquals("if (true) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) ");
    consumer.endStatement(true);
    assertEquals("if (true) {\n}\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) ");
    consumer.maybeEndStatement();
    assertEquals("if (true) {\n", consumer.getCode());
    consumer.append("}");
    consumer.maybeEndStatement();
    assertEquals("if (true) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\n}");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\nreturn 1;\n}");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}\nreturn 1;\n", consumer.getCode());
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
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testAddString() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.add("\"foo bar\"");
    assertEquals("\"foo bar\"", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.getCode());
    consumer.addOp("+", true);
    assertEquals("+\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1);
    assertEquals("1", consumer.getCode());
    consumer.addNumber(1.5);
    assertEquals("1.5", consumer.getCode());
    consumer.addNumber(-1.5);
    assertEquals("-1.5", consumer.getCode());
    consumer.addNumber(1e10);
    assertEquals("1e10", consumer.getCode());
    consumer.addNumber(1e-10);
    assertEquals("1e-10", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    consumer.append("if (true) {\nreturn 1;\n}");
    assertEquals("if (true) {\n  return 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };
    consumer.append("for (int i = 0; i < 10; i++) {\n}");
    consumer.endBlockFor(true);
    assertEquals("for (int i = 0; i < 10; i++) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private String getCode() {
    return consumer.getCode();
  }
}