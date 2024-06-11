import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello world");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello world");
    assertEquals("hello world", consumer.getLastCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeLineBreak();
    assertEquals("hello\n", consumer.getLastCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeCutLine();
    assertEquals("hello", consumer.getLastCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getLastCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getLastCode());
  }

  @Test
  public void testEndBlock_false() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(false);
    assertEquals("}", consumer.getLastChar());
    assertEquals("}", consumer.getLastCode());
  }

  @Test
  public void testEndBlock_true() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endLine();
    consumer.endBlock(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    assertEquals("hello,\n", consumer.getLastCode());
  }

  @Test
  public void testEndStatement_false() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(false);
    assertEquals("hello", consumer.getLastCode());
  }

  @Test
  public void testEndStatement_true() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(true);
    assertEquals("hello;\n", consumer.getLastCode());
  }

  @Test
  public void testEndFunction_false() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(false);
    assertEquals("}", consumer.getLastChar());
    assertEquals("}", consumer.getLastCode());
  }

  @Test
  public void testEndFunction_true() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endLine();
    consumer.endFunction(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("}\n", consumer.getLastCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getLastCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals("", consumer.getLastCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.add(" world");
    assertEquals("hello world", consumer.getLastCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.addOp("+", false);
    consumer.append("world");
    assertEquals("hello + world", consumer.getLastCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.addNumber(1);
    assertEquals("hello 1", consumer.getLastCode());
    consumer.addNumber(2.5);
    assertEquals("hello 1.5", consumer.getLastCode());
    consumer.addNumber(0.0);
    assertEquals("hello", consumer.getLastCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor_true() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node n = new Node();
    assertTrue(consumer.breakAfterBlockFor(n, true));
  }

  @Test
  public void testBreakAfterBlockFor_false() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node n = new Node();
    assertFalse(consumer.breakAfterBlockFor(n, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getLastCode());
  }

  private static String getLastCode(CodeConsumer consumer) {
    StringBuilder sb = new StringBuilder();
    while (!consumer.statementStarted) {
      sb.append(consumer.getLastChar());
      consumer.maybeEndStatement();
    }
    return sb.toString();
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder stringBuilder = new StringBuilder();

  @Override
  char getLastChar() {
    return stringBuilder.charAt(stringBuilder.length() - 1);
  }

  @Override
  String getLastCode() {
    return stringBuilder.toString();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
    stringBuilder.append(getLastCode());
  }
}