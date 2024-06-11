import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method does not throw any exception
    consumer.continueProcessing();
    consumer.endSourceMapping(node);
    consumer.continueProcessing();
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if endSourceMapping method does not throw any exception
    consumer.continueProcessing();
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Check if continueProcessing method returns true by default
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    char lastChar = consumer.getLastChar();
    assertEquals('t', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    String expected = "hello";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    consumer.endLine();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expected = "{";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    String expected = "{\ntest\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expected = "test1,\n test2";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.append("test2");
    consumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    String expected = "test;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expected = "test\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    String expected = "function test() {\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endFunction(true);
    String expected = "function test() {\n return 1;\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    String expected = ":";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expected = ":";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test1");
    consumer.add(" ");
    consumer.addIdentifier("test2");
    String expected = "test1 test2";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addOp("+", false);
    consumer.append("test2");
    String expected = "test1 + test2";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addOp("==", true);
    consumer.append("test2");
    String expected = "test1 == test2";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.1);
    String expected = "1.1";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public String getCode() {
    return super.code;
  }
}