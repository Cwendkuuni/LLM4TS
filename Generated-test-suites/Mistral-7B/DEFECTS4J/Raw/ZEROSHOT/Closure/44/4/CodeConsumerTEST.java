import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
  }

  @org.junit.Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testContinueProcessingWhenFalse() {
    CodeConsumer consumer = new TestCodeConsumerWithContinueProcessingFalse();

    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\n test\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlockWithShouldEndLineTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\n test\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlockWithShouldEndLineFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(false);
    assertEquals("{\n test", consumer.getCode());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test1,\n test2", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatementWithStatementNeedsSemicolonTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test;");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatementWithStatementNeedsSemicolonFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endStatement(false);
    assertEquals("test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatementWithStatementNeedsSemicolonTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test;");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatementWithStatementNeedsSemicolonFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContextTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.append("test();");
    consumer.endFunction(true);
    assertEquals("function test() {\n test();\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContextFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.append("test();");
    consumer.endFunction(false);
    assertEquals("function test() {\n test();", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals(":\n test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.add(" test");
    assertEquals("test test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("test");
    assertEquals("test + test\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.addNumber(1.234);
    assertEquals("test 1.234\n", consumer.getCode());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumerWithShouldPreserveExtraBlocksTrue();

    assertEquals(true, consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocksFalse() {
    CodeConsumer consumer = new TestCodeConsumerWithShouldPreserveExtraBlocksFalse();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @org.junit.Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);

    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private final StringBuilder code = new StringBuilder();

    @Override
    public void append(String str) {
      code.append(str);
    }

    @Override
    public void appendBlockStart() {
      code.append("{");
    }

    @Override
    public void appendBlockEnd() {
      code.append("}");
    }

    @Override
    public void startNewLine() {
      code.append("\n");
    }

    @Override
    public void maybeLineBreak() {
      startNewLine();
    }

    @Override
    public void maybeCutLine() {
      startNewLine();
    }

    @Override
    public void endLine() {
      startNewLine();
    }

    @Override
    public void notePreferredLineBreak() {
      startNewLine();
    }

    @Override
    public void beginBlock() {
      appendBlockStart();
    }

    @Override
    public void endBlock(boolean shouldEndLine) {
      if (shouldEndLine) {
        endBlock(true);
      } else {
        appendBlockEnd();
      }
    }

    @Override
    public void listSeparator() {
      append(",\n");
    }

    @Override
    public void endStatement() {
      append(";");
      maybeLineBreak();
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      if (needSemiColon) {
        endStatement();
      } else if (statementStarted) {
        statementStarted = false;
      }
    }

    @Override
    public void maybeEndStatement() {
      if (statementNeedsEnded) {
        append(";");
        maybeLineBreak();
        statementNeedsEnded = false;
      } else if (statementStarted) {
        statementStarted = false;
      }
    }

    @Override
    public void endFunction() {
      // empty
    }

    @Override
    public void endFunction(boolean statementContext) {
      // empty
    }

    @Override
    public void beginCaseBody() {
      // empty
    }

    @Override
    public void endCaseBody() {
      // empty
    }

    @Override
    public void add(String newcode) {
      code.append(newcode);
    }

    @Override
    public void appendOp(String op, boolean binOp) {
      code.append(op);
    }

    @Override
    public void addOp(String op, boolean binOp) {
      code.append(op);
    }

    @Override
    public void addNumber(double x) {
      code.append(String.valueOf(x));
      code.append("\n");
    }

    @Override
    public void startSourceMapping(Node node) {
      // empty
    }

    @Override
    public void endSourceMapping(Node node) {
      // empty
    }

    @Override
    public boolean continueProcessing() {
      return true;
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    public String getCode() {
      return code.toString();
    }
  }

  @org.junit.Test
  public void testTestCodeConsumerWithContinueProcessingFalse() {
    CodeConsumer consumer = new TestCodeConsumerWithContinueProcessingFalse();

    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testTestCodeConsumerWithShouldPreserveExtraBlocksFalse() {
    CodeConsumer consumer = new TestCodeConsumerWithShouldPreserveExtraBlocksFalse();

    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  private static class TestCodeConsumerWithContinueProcessingFalse extends CodeConsumer {
    public TestCodeConsumerWithContinueProcessingFalse() {
      statementNeedsEnded = true;
    }
  }

  private static class TestCodeConsumerWithShouldPreserveExtraBlocksTrue extends CodeConsumer {
    public TestCodeConsumerWithShouldPreserveExtraBlocksTrue() {
      statementNeedsEnded = true;
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      return true;
    }
  }

  private static class TestCodeConsumerWithShouldPreserveExtraBlocksFalse extends CodeConsumer {
    public TestCodeConsumerWithShouldPreserveExtraBlocksFalse() {
      statementNeedsEnded = true;
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      return false;
    }
  }
}