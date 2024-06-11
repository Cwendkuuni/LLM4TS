import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot directly assert on private fields, so we'll just check if the method was called
    assertTrue(consumer.sourceMappingStarted);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot directly assert on private fields, so we'll just check if the method was called
    assertFalse(consumer.sourceMappingStarted);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.code);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    // Cannot directly assert on private fields, so we'll just check if the method was called
    assertTrue(consumer.cutLineCalled);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot directly assert on private fields, so we'll just check if the method was called
    assertTrue(consumer.preferredLineBreakCalled);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.code);
  }

  @Test(expected = AssertionError.class)
  public void testMaybeEndStatementWithoutStatementStarted() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeEndStatement();
    fail("Should have thrown an AssertionError");
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals("", consumer.code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addNumber(123.45);
    assertEquals("123.45", consumer.code);
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(-0.0));
    assertEquals(true, isNegativeZero(new Double(-0.0)));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('0'));
    assertFalse(isWordChar(','));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.code);
  }

  @Test(expected = AssertionError.class)
  public void testEndStatementWithoutStatementStarted() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement();
    fail("Should have thrown an AssertionError");
  }

  @Test
  public void testEndFunctionWithoutStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction();
    fail("Should have thrown an AssertionError");
  }
}

class DummyCodeConsumer extends CodeConsumer {
  boolean sourceMappingStarted = false;
  boolean cutLineCalled = false;
  boolean preferredLineBreakCalled = false;
  String code = "";

  @Override
  public void startSourceMapping(Node node) {
    super.startSourceMapping(node);
    sourceMappingStarted = true;
  }

  @Override
  public void endSourceMapping(Node node) {
    super.endSourceMapping(node);
    code = "";
  }

  @Override
  public boolean continueProcessing() {
    return false;
  }

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public void add(String newcode) {
    super.add(newcode);
    code += newcode;
  }

  @Override
  public void append(String str) {
    super.append(str);
    code += str;
  }

  @Override
  public void appendBlockStart() {
    super.appendBlockStart();
  }

  @Override
  public void appendBlockEnd() {
    super.appendBlockEnd();
  }

  @Override
  public void startNewLine() {
    super.startNewLine();
    code += "\n";
  }

  @Override
  public void maybeLineBreak() {
    super.maybeLineBreak();
    cutLineCalled = true;
  }

  @Override
  public void endLine() {
    super.endLine();
  }

  @Override
  public void notePreferredLineBreak() {
    super.notePreferredLineBreak();
    preferredLineBreakCalled = true;
  }

  @Override
  public void beginBlock() {
    super.beginBlock();
  }

  @Override
  public void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
  }

  @Override
  public void listSeparator() {
    super.listSeparator();
  }

  @Override
  public void endStatement() {
    super.endStatement();
  }

  @Override
  public void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
  }

  @Override
  public void maybeEndStatement() {
    super.maybeEndStatement();
  }

  @Override
  public void endFunction() {
    super.endFunction();
  }

  @Override
  public void endFunction(boolean statementContext) {
    super.endFunction(statementContext);
  }

  @Override
  public void beginCaseBody() {
    super.beginCaseBody();
  }

  @Override
  public void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  public void addIdentifier(String identifier) {
    super.addIdentifier(identifier);
    code += identifier;
  }

  @Override
  public void appendOp(String op, boolean binOp) {
    super.appendOp(op, binOp);
    code += op;
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
    code += String.valueOf(x);
  }

  @Override
  public boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return false;
  }

  @Override
  public void endFile() {
    super.endFile();
  }
}