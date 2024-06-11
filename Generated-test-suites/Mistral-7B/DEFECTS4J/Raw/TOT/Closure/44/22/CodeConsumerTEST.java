import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    when(consumer.continueProcessing()).thenReturn(true);

    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");

    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("Hello");
    assertEquals("Hello", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.maybeLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("Hello");
    consumer.endBlock();
    assertEquals("Hello}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("Hello");
    consumer.endBlock(true);
    assertEquals("Hello}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("value1");
    consumer.listSeparator();
    consumer.append("value2");

    assertEquals("value1,\nvalue2", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("if (true) {\n}");
    consumer.endStatement();

    assertEquals("if (true) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("if (true) {\n}");
    consumer.endStatement(true);

    assertEquals("if (true) {\n};\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("if (true) {\n}");
    consumer.maybeEndStatement();

    assertEquals("if (true) {\n}", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("function foo() {\n}");
    consumer.endFunction();
    consumer.append("return 1;");
    consumer.endFunction(true);

    assertEquals("function foo() {\n}\nreturn 1;\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("switch (x) {\ncase 1:\n}");
    consumer.beginCaseBody();

    assertEquals("switch (x) {\ncase 1:\n\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("switch (x) {\ncase 1:\n}");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();

    assertEquals("switch (x) {\ncase 1:\n\nreturn 1;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("function foo() {\n");
    consumer.addIdentifier("x");
    consumer.addIdentifier("y");
    consumer.append("}");

    assertEquals("function foo() {\nxs, y\n}", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("function foo() {\n");
    consumer.addNumber(123);
    consumer.append("}");

    assertEquals("function foo() {\n123\n}", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();

    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);

    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();

    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();

    consumer.append("function foo() {\n}");
    consumer.endFile();

    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  private static class StubCodeConsumer extends CodeConsumer {
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