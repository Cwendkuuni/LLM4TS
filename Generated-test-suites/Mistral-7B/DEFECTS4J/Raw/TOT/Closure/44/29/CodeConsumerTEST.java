import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    String expected = "// Source mapping for line 1\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
    consumer.beginBlock();
    consumer.append("Hello World");
    consumer.endBlock();
    String expected = "{\nHello World\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    char expected = 'd';
    char actual = consumer.getLastChar();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    String expected = "Hello World";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {\n");
    String expected = "function test() {\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("}");
    consumer.appendBlockEnd();
    String expected = "}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    consumer.startNewLine();
    consumer.append("Hello Again");
    String expected = "Hello World\nHello Again";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    consumer.maybeLineBreak();
    consumer.append("Hello Again");
    String expected = "Hello World\nHello Again";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    consumer.maybeCutLine();
    consumer.append("Hello Again");
    String expected = "Hello World\nHello Again";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    consumer.endLine();
    consumer.append("Hello Again");
    String expected = "Hello World\nHello Again";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {\n");
    consumer.append("  return 1;\n");
    consumer.endBlock();
    String expected = "{\nfunction test() {\n  return 1;\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {\n");
    consumer.append("  return 1;\n}");
    String expected = "{\nfunction test() {\n  return 1;\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {\n");
    consumer.endBlock(true);
    String expected = "{\nfunction test() {\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("[1, 2, 3]");
    consumer.listSeparator();
    consumer.append(7);
    String expected = "[1, 2, 3, 7]";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("return 1;");
    consumer.endStatement();
    String expected = "return 1;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("return 1;");
    consumer.endStatement(true);
    String expected = "return 1;\n";
    String actual = consumer.getCode();
    assertEquals(expect, actual);
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("return 1;");
    consumer.maybeEndStatement();
    String expected = "return 1;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {\n");
    consumer.endFunction();
    String expected = "{\nfunction test() {\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {\n");
    consumer.endFunction(true);
    String expected = "{\nfunction test() {\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {\ncase 1:\n");
    String expected = "switch (x) {\ncase 1:\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {\ncase 1:\n");
    consumer.endCaseBody();
    String expected = "switch (x) {\ncase 1:\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("1 + 2 + 3");
    String expected = "1 + 2 + 3";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("x");
    String expected = "x";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.5);
    String expected = "1.5";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", true);
    consumer.addNumber(1.5);
    consumer.addOp("+", false);
    consumer.addNumber(2.5);
    String expected = "1.5 + 2.5";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIdentifier("x");
    assertTrue(consumer.breakAfterBlockFor(node, false));
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIdentifier("x");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return super.getLastChar();
    }

    @Override
    String getCode() {
      return super.getCode();
    }
  }
}