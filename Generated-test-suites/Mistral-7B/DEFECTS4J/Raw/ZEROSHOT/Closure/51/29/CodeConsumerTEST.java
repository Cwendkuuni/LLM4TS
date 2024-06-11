import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(true, consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.startNewLine();
    consumer.append("newline");

    assertEquals("test\nnewline", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode().substring(consumer.getCode().length() - 1));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endLine();

    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endBlock();
    consumer.append("newline");

    assertEquals("{\n test\n}\nnewline", consumer.getCode());
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endBlock(true);
    consumer.append("newline");

    assertEquals("{\n test\n}\nnewline", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");

    assertEquals("test1,\n test2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endStatement();
    consumer.append("newline");

    assertEquals("test;\nnewline", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test");
    consumer.endStatement(true);
    consumer.append("newline");

    assertEquals("test;\nnewline", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.maybeEndStatement();
    consumer.append("test2");

    assertEquals("test1;\ntest2", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.endFunction();
    consumer.append("}");

    assertEquals("function test() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.endFunction(true);
    consumer.append("}");

    assertEquals("function test() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();

    assertEquals(":\n test\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.add("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addIdentifier("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addOp("+", false);
    assertEquals(" +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addNumber(42.0);
    assertEquals("42.0\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('0'));
    assertEquals(false, CodeConsumer.isWordChar('@'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.isBlock()).thenReturn(true);

    assertEquals(true, consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.isBlock()).thenReturn(false);

    assertEquals(false, consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void append(String str) {
      code.append(str);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }
}