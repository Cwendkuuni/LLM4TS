import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);

    // No verification needed, as this is only a setup for other tests
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);

    // No verification needed, as this is only a setup for other tests
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    boolean result = consumer.continueProcessing();

    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public char getLastChar() {
        return 'a';
      }
    };

    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("Hello, World!");

    assertEquals("Hello, World!", consumer.getCode());
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

    consumer.maybeLineBreak();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeCutLine();

    assertEquals("", consumer.getCode());
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

    assertEquals("", consumer.getCode());
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

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();

    assertEquals("{\n  test\n}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);

    assertEquals("{\n  test\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addIdentifier("item1");
    consumer.listSeparator();
    consumer.addIdentifier("item2");

    assertEquals("item1,\n  item2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("if (x < 5) {\n  y = 10;\n}");

    consumer.endStatement();

    assertEquals("if (x < 5) {\n  y = 10;\n};\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("if (x < 5) {\n  y = 10;\n}");

    consumer.endStatement(true);

    assertEquals("if (x < 5) {\n  y = 10;\n};\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("if (x < 5) {\n  y = 10;\n}");

    consumer.maybeEndStatement();

    assertEquals("if (x < 5) {\n  y = 10;\n}", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("function testFunction() {\n  return 42;\n}");
    consumer.endFunction();

    assertEquals("{\n  function testFunction() {\n    return 42;\n  }\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("function testFunction() {\n  return 42;\n}");
    consumer.endFunction(true);

    assertEquals("{\n  function testFunction() {\n    return 42;\n  }\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("switch (x) {\n  case 1:\n    break;\n  default:\n    break;\n}");
    consumer.beginCaseBody();
    consumer.append("y = 10;\n");
    consumer.endCaseBody();

    assertEquals("switch (x) {\n  case 1:\n    break;\n  case 1:\n    y = 10;\n    break;\n  default:\n    break;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addIdentifier("x");
    consumer.addIdentifier("y");

    assertEquals("x, y\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addOp("+", false);
    consumer.addIdentifier("x");
    consumer.addOp("+", false);
    consumer.addIdentifier("y");

    assertEquals("x + y\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addNumber(42);

    assertEquals("42\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(true, consumer.isWordChar('a'));
    assertEquals(true, consumer.isWordChar('_'));
    assertEquals(true, consumer.isWordChar('$'));
    assertEquals(true, consumer.isWordChar('A'));
    assertEquals(true, consumer.isWordChar('Z'));
    assertEquals(true, consumer.isWordChar('1'));
    assertEquals(true, consumer.isWordChar('9'));
    assertEquals(false, consumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    when(node.getClassName()).thenReturn("statement");

    boolean result = consumer.breakAfterBlockFor(node, true);

    assertEquals(true, result);
  }

  @Test
  public void testBreakAfterBlockForExpression() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    when(node.getClassName()).thenReturn("expression");

    boolean result = consumer.breakAfterBlockFor(node, true);

    assertEquals(false, result);
  }

  @Test
  public void testBreakAfterBlockIfStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    when(node.getClassName()).thenReturn("statement");

    boolean result = consumer.breakAfterBlockFor(node, false);

    assertEquals(false, result);
  }

  @Test
  public void testBreakAfterBlockIfExpression() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    when(node.getClassName()).thenReturn("expression");

    boolean result = consumer.breakAfterBlockFor(node, false);

    assertEquals(true, result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();

    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    public void append(String str) {
      code.append(str);
    }

    @Override
    public void add(String newCode) {
      code.append(newCode);
    }

    @Override
    public void addOp(String op, boolean binOp) {
      code.append(op);
    }

    @Override
    public void addNumber(double x) {
      code.append(x);
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    public String getCode() {
      return code.toString();
    }
  }
}