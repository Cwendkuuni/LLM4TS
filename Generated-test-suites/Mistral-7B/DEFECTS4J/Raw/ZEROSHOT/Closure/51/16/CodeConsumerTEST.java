import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions not possible as startSourceMapping does not have any return value
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Assertions not possible as endSourceMapping does not have any return value
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("string");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("string", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("statement");
    consumer.endBlock();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("{\nstatement\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("statement");
    consumer.endBlock(true);
    consumer.endLine(); // needed for pretty-printing
    assertEquals("{\nstatement\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("first");
    consumer.listSeparator();
    consumer.append("second");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("first,\nsecond\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("statement");
    consumer.endLine(); // needed for pretty-printing
    consumer.endStatement();
    consumer.append("nextStatement");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("statement;\nnextStatement\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("statement;");
    consumer.endLine(); // needed for pretty-printing
    consumer.endStatement();
    consumer.append("nextStatement");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("statement;\nnextStatement\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("statement");
    consumer.maybeEndStatement();
    consumer.append("nextStatement");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("statement;\nnextStatement\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function functionName() {");
    consumer.append("statement");
    consumer.endFunction();
    consumer.endLine(); // needed for pretty-printing
    assertEquals("function functionName() {\nstatement\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function functionName() {");
    consumer.append("statement");
    consumer.endFunction(true);
    consumer.endLine(); // needed for pretty-printing
    assertEquals("function functionName() {\nstatement\n}", consumer.getCode());
    consumer.append("nextStatement");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("function functionName() {\nstatement\nnextStatement\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (expression) {");
    consumer.beginCaseBody();
    consumer.append("case value:");
    consumer.endLine(); // needed for pretty-printing
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endLine(); // needed for pretty-printing
    consumer.append("}");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("switch (expression) {\n  case value:\n}\nbreak;\n}", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (expression) {");
    consumer.beginCaseBody();
    consumer.append("case value:");
    consumer.endLine(); // needed for pretty-printing
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("switch (expression) {\n  case value:\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("statement1");
    consumer.add(" statement2");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("statement1 statement2\n", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("2");
    consumer.appendOp("+", true);
    consumer.append("3");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("2 + 3\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("2");
    consumer.addOp("+", true);
    consumer.append("3");
    consumer.endLine(); // needed for pretty-printing
    assertEquals("2 + 3\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1.5);
    consumer.endLine(); // needed for pretty-printing
    assertEquals("1.5\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('1'));
    assertFalse(isWordChar('*'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
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
    consumer.endLine(); // needed for pretty-printing
    assertEquals("", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testThrowAssertionError() {
    throw new AssertionError();
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return '\0';
  }

  @Override
  String getCode() {
    StringBuilder code = new StringBuilder();
    code.append(super.getCode());
    return code.toString();
  }
}