import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // The implementation of this method is empty, so no verification is needed.
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // The implementation of this method is empty, so no verification is needed.
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test with an empty codebase
    assertNull(consumer.getLastChar());

    consumer.append("Hello");
    assertEquals('l', consumer.getLastChar());

    consumer.endLine();
    consumer.notePreferredLineBreak();

    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.codeToString());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("Hello");
    assertEquals("Hello", consumer.codeToString());

    consumer.append(" World");
    assertEquals("Hello World", consumer.codeToString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.codeToString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.codeToString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.codeToString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.codeToString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertNull(consumer.codeToString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.codeToString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.codeToString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.codeToString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("Hello");
    consumer.endBlock();
    assertEquals("{\nHello}\n", consumer.codeToString());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("Hello");
    consumer.endBlock(true);
    assertEquals("{\nHello}\n", consumer.codeToString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.listSeparator();
    consumer.append("World");
    assertEquals("Hello,\nWorld", consumer.codeToString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.endStatement();
    assertEquals("Hello;\n", consumer.codeToString());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.endStatement(true);
    assertEquals("Hello;\n", consumer.codeToString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.append(";");
    consumer.maybeEndStatement();
    assertEquals("Hello", consumer.codeToString());

    consumer.append(";"); // second semicolon
    consumer.maybeEndStatement();
    assertEquals("Hello;", consumer.codeToString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append(" return 42;");
    consumer.endFunction();
    assertEquals("function foo() {\n return 42;\n}\n", consumer.codeToString());
  }

  @Test
  public void testEndFunctionWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append(" return 42;");
    consumer.endFunction(true);
    assertEquals("function foo() {\n return 42;\n}\n", consumer.codeToString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {\n case 1:\n");
    consumer.beginCaseBody();
    consumer.append(" return 42;\n");
    consumer.endCaseBody();
    consumer.append(" break;\n");
    consumer.append("}");
    assertEquals("switch (x) {\n case 1:\n case 1:\n return 42;\n break;\n}", consumer.codeToString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello");
    consumer.add("World");
    assertEquals("HelloWorld", consumer.codeToString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", true);
    consumer.append("y");
    assertEquals("x + y", consumer.codeToString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.addNumber(1);
    consumer.addNumber(0.5);
    consumer.addNumber(-0.1);
    assertEquals("x 1.0 -0.1", consumer.codeToString());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('1'));
    assertFalse(consumer.isWordChar('!'));
    assertFalse(consumer.isWordChar('%'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.codeToString());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    void append(String str) {
      super.append(str);
      code.append(str);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String codeToString() {
      return code.toString();
    }

    StringBuilder code = new StringBuilder();
  }
}