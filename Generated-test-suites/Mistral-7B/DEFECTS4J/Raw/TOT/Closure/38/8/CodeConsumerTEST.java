import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
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
    consumer.append("hello world");
    consumer.maybeLineBreak();
    consumer.endLine();
    String output = consumer.getOutput();
    assertEquals("hello world\n", output);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String output = consumer.getOutput();
    assertEquals("\n", output);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("This line is too long.");
    consumer.maybeCutLine();
    String output = consumer.getOutput();
    assertEquals("This line is too long.\n", output);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("This is a line.");
    consumer.endLine();
    String output = consumer.getOutput();
    assertEquals("This is a line.\n", output);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    String output = consumer.getOutput();
    assertNotNull(output);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String output = consumer.getOutput();
    assertEquals("{\n", output);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("this is a block\n");
    consumer.appendBlockEnd();
    String output = consumer.getOutput();
    assertEquals("{\nthis is a block\n}\n", output);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("item1");
    consumer.listSeparator();
    consumer.append("item2");
    String output = consumer.getOutput();
    assertEquals("item1,\nitem2\n", output);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.maybeEndStatement();
    consumer.append("int y = 2;\n");
    consumer.maybeEndStatement();
    String output = consumer.getOutput();
    assertEquals("int x = 1;\nint y = 2;\n", output);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {\n");
    consumer.append("  int x = 1;\n");
    consumer.endFunction();
    String output = consumer.getOutput();
    assertEquals("function foo() {\n  int x = 1;\n}\n", output);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (x) {\n");
    consumer.beginCaseBody();
    consumer.append("  case 1: break;\n");
    consumer.endCaseBody();
    String output = consumer.getOutput();
    assertEquals("switch (x) {\n  case 1:\n}", output);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
  }
}

class StubCodeConsumer extends CodeConsumer {
  private StringBuilder output = new StringBuilder();

  @Override
  void append(String str) {
    output.append(str);
  }

  @Override
  void add(String newcode) {
    append(newcode);
  }

  @Override
  char getLastChar() {
    return output.charAt(output.length() - 1);
  }

  @Override
  String getOutput() {
    return output.toString();
  }
}