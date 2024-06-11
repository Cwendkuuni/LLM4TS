import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    assertTrue(consumer.statementStarted);
    assertTrue(consumer.statementNeedsEnded);

    when(consumer.continueProcessing()).thenReturn(false);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementStarted);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.continueProcessing());

    consumer.statementNeedsEnded = true;

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    assertEquals('o', consumer.getLastChar());

    consumer.append("world");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.append(" world");

    assertEquals("hello world", consumer.code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.endStatement();

    assertEquals("hello;", consumer.code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("functionFunc()");
    consumer.endFunction();

    assertEquals("function func() {\n}", consumer.code);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("var a = 5;");
    consumer.endBlock();

    assertEquals("{\nvar a = 5;\n}", consumer.code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("var a = 5;");
    consumer.endBlock();
    consumer.append("var b = 10;");
    consumer.endBlock();

    assertEquals("{\nvar a = 5;\n}\n{\nvar b = 10;\n}", consumer.code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("var a = 5,");
    consumer.listSeparator();
    consumer.addIdentifier("var b = 10;");

    assertEquals("var a = 5,\nvar b = 10;\n", consumer.code);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("var a = 5;");
    consumer.endFile();

    assertEquals("var a = 5;\n", consumer.code);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("if (x > 5) {\n x++;\n}");

    assertFalse(consumer.shouldPreserveExtraBlocks());

    consumer = new TestSingleStatementCodeConsumer();
    consumer.beginBlock();
    consumer.addIdentifier("if (x > 5) x++;");

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(consumer.breakAfterBlockFor(node, true)).thenReturn(false);

    consumer.beginBlock();
    consumer.addIdentifier("if (x > 5) {\nsum++;\n}");

    assertEquals("if (x > 5) {\nsum++;\n}", consumer.code);

    consumer.beginBlock();
    consumer.addIdentifier("function func() {\n}");

    assertTrue(consumer.code.endsWith("\n}"));
  }

  private static class TestCodeConsumer extends CodeConsumer {
    String code = "";

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }
  }

  private static class TestSingleStatementCodeConsumer extends TestCodeConsumer {
    @Override
    boolean shouldPreserveExtraBlocks() {
      return true;
    }
  }

  private void assertEquals(String expected, String actual) {
    assertEquals(expected, actual);
  }
}