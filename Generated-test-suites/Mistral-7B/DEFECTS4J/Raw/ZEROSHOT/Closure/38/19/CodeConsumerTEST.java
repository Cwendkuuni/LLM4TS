import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verification not possible as startSourceMapping is an abstract method
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Verification not possible as endSourceMapping is an abstract method
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());
    consumer.statementNeedsEnded = true;
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("test");
    consumer.appendBlockEnd();
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("test");
    consumer.startNewLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("test");
    consumer.notePreferredLineBreak();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.append("test");
    consumer.beginBlock();
    assertEquals("test{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("test{\n test\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test1,\n test2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("test2");
    assertEquals("test;\ntest2\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function test() {}");
    consumer.endFunction();
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("function test() {\n test2();\n}");
    consumer.endFunction(true);
    assertEquals("function test() {\n test2();\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("case label:\n test()");
    consumer.beginCaseBody();
    assertEquals("case label:\n test(){\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("case label:\n test(){\n test2();\n}");
    consumer.endCaseBody();
    assertEquals("case label:\n test(){\n test2();\n}\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("test");
    consumer.add(" test2");
    assertEquals("test test2\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("test");
    consumer.addOp("+");
    consumer.add(" test2");
    assertEquals("test + test2\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("test");
    consumer.addNumber(1.23);
    assertEquals("test 1.23\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0.0));
    assertEquals(true, isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('1'));
    assertTrue(isWordChar('Z'));
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar('+'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public void add(String newcode) {
    super.add(newcode);
  }

  @Override
  public void append(String str) {
    super.append(str);
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
  }

  @Override
  public void maybeLineBreak() {
    super.maybeLineBreak();
  }

  @Override
  public void maybeCutLine() {
    super.maybeCutLine();
  }

  @Override
  public void endLine() {
    super.endLine();
  }

  @Override
  public void notePreferredLineBreak() {
    super.notePreferredLineBreak();
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
  public void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public void appendOp(String op, boolean binOp) {
    super.appendOp(op, binOp);
  }

  @Override
  public void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void endFile() {
    super.endFile();
  }

  @Override
  public String getCode() {
    return super.getCode();
  }
}