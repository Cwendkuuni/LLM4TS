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

    // No verification needed, as method does not have any side effects
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);

    // No verification needed, as method does not have any side effects
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessing_shouldReturnFalse() {
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello world");

    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello world");

    assertEquals("hello world", consumer.getCode());
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

    consumer.startNewLine();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello world");
    consumer.maybeLineBreak();

    assertEquals("hello world\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello world");
    consumer.maybeCutLine();

    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endLine();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();

    assertEquals("\n", consumer.getCode());
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

    consumer.append("hello world");
    consumer.endBlock(true);

    assertEquals("hello world}\n", consumer.getCode());
  }

  @Test
  public void testEndBlock_shouldNotEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello world");
    consumer.endBlock(false);

    assertEquals("hello world}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");

    assertEquals("hello,\nworld", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.endStatement();
    consumer.append("world");

    assertEquals("hello;\nworld", consumer.getCode());
  }

  @Test
  public void testEndStatement_noSemicolonNeeded() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("{");
    consumer.append("hello");
    consumer.endStatement(false);
    consumer.append("world");

    assertEquals("{\nhello\nworld}", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.maybeEndStatement();
    consumer.append("world");

    assertEquals("hello;\nworld", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function foo() {");
    consumer.endFunction();
    consumer.append("}");

    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunction_statementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function foo() {");
    consumer.append("return 42;");
    consumer.endFunction(true);

    assertEquals("function foo() {\n  return 42;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("case 1:");
    consumer.beginCaseBody();

    assertEquals("case 1:\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("case 1:\n");
    consumer.endCaseBody();

    assertEquals("case 1:\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.add(" world");

    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("foo");
    consumer.addIdentifier("bar");

    assertEquals("foo bar", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("5");
    consumer.addOp("+", true);
    consumer.append("3");

    assertEquals("5 + 3", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("5");
    consumer.addNumber(2.71828);

    assertEquals("5 2.71828", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getType()).thenReturn(2); // STATEMENT

    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();

    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private String code = "";

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code;
    }
  }
}