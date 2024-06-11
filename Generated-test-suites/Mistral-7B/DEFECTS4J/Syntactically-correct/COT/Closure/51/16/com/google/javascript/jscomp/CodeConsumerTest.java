import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer = new TestCodeConsumer();

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if the method was called
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if the methods were called in order and no extra ';' was added
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("Hello");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    StringBuilder actual = new StringBuilder();
    consumer.append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testAppend() {
    consumer.append("Hello");
    String expected = "Hello";
    StringBuilder actual = new StringBuilder();
    consumer.append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    consumer.endBlock(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(",", consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    consumer.append("if (true)");
    consumer.endStatement();
    consumer.maybeLineBreak();
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
    assertEquals("if (true)\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("if (true)");
    consumer.endStatement(true);
    consumer.maybeLineBreak();
    assertTrue(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
    assertEquals("if (true)\n;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("if (true)");
    consumer.maybeEndStatement();
    consumer.append("else ");
    consumer.maybeEndStatement();
    assertEquals("if (true)\nelse \n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function test(){}");
    consumer.endFunction();
    consumer.maybeLineBreak();
    assertEquals("function test(){\n}\n", consumer.getCode());
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("function test(){}");
    consumer.endFunction(true);
    consumer.maybeLineBreak();
    assertEquals("function test(){\n}\n", consumer.getCode());
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (1) {\n case 1:\n}");
    consumer.beginCaseBody();
    consumer.append("System.out.println(\"Case 1\");");
    consumer.endCaseBody();
    consumer.maybeLineBreak();
    assertEquals("switch (1) {\n case 1:\n System.out.println(\"Case 1\");\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("String a = \"Hello\";");
    consumer.add("String b = \"World\";");
    consumer.maybeLineBreak();
    assertEquals("String a = \"Hello\";\nString b = \"World\";\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("int x = 5;");
    consumer.addOp("+", true);
    consumer.addNumber(2);
    consumer.maybeLineBreak();
    assertEquals("int x = 5;\nint y = 7;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("int x = 5;");
    consumer.addNumber(2.5);
    consumer.maybeLineBreak();
    assertEquals("int x = 5;\nint y = 7;\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer.append("for (int i = 0; i < 10; i++) {\n System.out.println(i);\n}");
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    consumer.append("if (true) {");
    consumer.append("System.out.println(\"Before\");");
    boolean result = consumer.breakAfterBlockFor(node, false);
    assertFalse(result);
    assertEquals("if (true) {\n System.out.println(\"Before\");\n}", consumer.getCode());

    consumer.append("}");
    result = consumer.breakAfterBlockFor(node, true);
    assertTrue(result);
    assertEquals("if (true) {\n System.out.println(\"Before\");\n}\n", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testEndFile() {
    consumer.endFile();
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    StringBuilder code = new StringBuilder();
    append(code);
    return code.charAt(code.length() - 1);
  }

  @Override
  String getCode() {
    StringBuilder code = new StringBuilder();
    append(code);
    return code.toString();
  }
}