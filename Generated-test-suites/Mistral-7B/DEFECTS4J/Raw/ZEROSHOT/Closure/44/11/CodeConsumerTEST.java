import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newEmpty();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    assertEquals('l', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.maybeLineBreak();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.endLine();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.appendBlockStart();
    consumer.append("world");
    consumer.appendBlockEnd();
    assertEquals("hello{\nworld\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");
    assertEquals("hello,\nworld", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = 5;");
    consumer.endStatement();
    assertEquals("x = 5;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = 5;");
    consumer.maybeEndStatement();
    consumer.append("y = 10;");
    assertEquals("x = 5;\ny = 10;", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("x = 5;");
    consumer.endCaseBody();
    assertEquals("case 1:\nx = 5;\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = ");
    consumer.add("5");
    assertEquals("x = 5", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = ");
    consumer.addIdentifier("foo");
    assertEquals("x = foo", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = ");
    consumer.addOp("+", false);
    consumer.add(5);
    assertEquals("x = 5", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = ");
    consumer.addNumber(5);
    assertEquals("x = 5", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test(expected = AssertionError.class)
  public void testBreakAfterBlockFor1() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newEmpty();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockFor2() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newText("if (x) {");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertEquals("if (x) {\n", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor3() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newText("if (x) { x = 5; }");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertEquals("if (x) {\n  x = 5;\n}", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor4() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newText("if (x) { x = 5; } else { y = 10; }");
    assertFalse(consumer.breakAfterBlockFor(node, true));
    assertEquals("if (x) {\n  x = 5;\n} else {\n  y = 10;\n}", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}

class DummyCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return '\0';
  }

  @Override
  public void append(String str) {
    super.append(str);
  }

  @Override
  public void add(String newcode) {
    super.add(newcode);
  }

  @Override
  public void appendOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  public String getCode() {
    return super.code;
  }
}