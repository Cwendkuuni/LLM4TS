import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // verify no exception was thrown and no code was generated
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // verify no exception was thrown and no code was generated
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    String code = consumer.getCode();
    assertEquals("hello", code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // verify no code was generated
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    // verify no code was generated
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // verify no code was generated
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    String code = consumer.getCode();
    assertEquals(",\n", code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.endStatement();
    String code = consumer.getCode();
    assertEquals("if (true)\n", code);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true);");
    String code = consumer.getCode();
    assertEquals("if (true);\n", code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.maybeEndStatement();
    String code = consumer.getCode();
    assertEquals("if (true)\n", code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();
    String code = consumer.getCode();
    assertEquals("function foo() {\n}\n", code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {\n return 1;\n}");
    consumer.endFunction(true);
    String code = consumer.getCode();
    assertEquals("function foo() {\n return 1;\n}\n", code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    String code = consumer.getCode();
    assertEquals(":\n", code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    String code = consumer.getCode();
    assertEquals(":\n", code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.add(" x = 1;");
    String code = consumer.getCode();
    assertEquals("if (true)\nx = 1;\n", code);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.addIdentifier("x");
    String code = consumer.getCode();
    assertEquals("if (true)x;\n", code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.addOp("+", false);
    String code = consumer.getCode();
    assertEquals("if (true)+;\n", code);
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.addOp("+", true);
    String code = consumer.getCode();
    assertEquals("if (true) +\n", code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true)");
    consumer.addNumber(1.2);
    String code = consumer.getCode();
    assertEquals("if (true) 1.2\n", code);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test(expected = AssertionError.class)
  public void testSawFunctionIsSet() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {}");
    consumer.endFunction();
    consumer.sawFunction = false;
    consumer.append("function bar() {}");
    fail("Expected AssertionError");
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return '\u0000';
  }

  @Override
  public String getCode() {
    return super.code;
  }
}