import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumerTest;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @org.junit.Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @org.junit.Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @org.junit.Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @org.junit.Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar_Empty() {
    assertNull(consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend_Empty() {
    consumer.append("");
    assertEquals("", consumer.toString());
  }

  @org.junit.Test
  public void testAppend_String() {
    consumer.append("hello");
    assertEquals("hello", consumer.toString());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("test");
    consumer.appendBlockEnd();
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @org.junit.Test
  public void testStartNewLine() {
    consumer.append("hello");
    consumer.startNewLine();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    consumer.append("hello");
    consumer.maybeLineBreak();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    consumer.append("hello");
    consumer.maybeCutLine();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndLine() {
    consumer.append("hello");
    consumer.endLine();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    consumer.append("hello");
    consumer.notePreferredLineBreak();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @org.junit.Test
  public void testEndBlock_ShouldEndLine() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\ntest\n}", consumer.toString());
    assertEquals('\n', consumer.getLastChar());
  }

  @org.junit.Test
  public void testListSeparator() {
    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndStatement() {
    consumer.append("hello");
    consumer.endStatement();
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndStatement_WithSemiColon() {
    consumer.append("hello");
    consumer.endStatement(true);
    assertEquals("helloworld\n", consumer.toString());
    assertEquals('\n', consumer.getLastChar());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    consumer.append("hello");
    consumer.maybeEndStatement();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("hello");
    consumer.endFunction();
    assertEquals("{\nfunction test() {\n  hello\n}\n}", consumer.toString());
  }

  @org.junit.Test
  public void testEndFunction_StatementContext() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("hello");
    consumer.endFunction(true);
    assertEquals("{\nfunction test() {\n  hello\n}\n}", consumer.toString());
    assertEquals('\n', consumer.getLastChar());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    consumer.beginBlock();
    consumer.append("switch (x) { case 1:");
    consumer.beginCaseBody();
    consumer.append("hello");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("{\nswitch (x) {\n  case 1:\n    hello\n  break;\n}\n}", consumer.toString());
  }

  @org.junit.Test
  public void testAdd() {
    consumer.append("hello");
    consumer.add(" world");
    assertEquals("helloworld\n", consumer.toString());
  }

  @org.junit.Test
  public void testAddOp_BinOp() {
    consumer.append("1 + 2 + ");
    consumer.addOp("3 + 4", true);
    assertEquals("1 + 2 + 3 + 4\n", consumer.toString());
  }

  @org.junit.Test
  public void testAddOp_NoBinOp() {
    consumer.append("1 + 2 + ");
    consumer.addOp("3", false);
    assertEquals("1 + 2 + 3\n", consumer.toString());
  }

  @org.junit.Test
  public void testAddNumber() {
    consumer.append("1 + ");
    consumer.addNumber(2.5);
    assertEquals("1 + 2.5\n", consumer.toString());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockFor_StatementContext() {
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @org.junit.Test
  public void testBreakAfterBlockFor_NotStatementContext() {
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  @org.junit.Test
  public void testEndFile() {
    consumer.append("test");
    consumer.endFile();
    assertEquals("test\n", consumer.toString());
  }

  @org.junit.Test
  public void testStatementNeedsEndedAndStatementStarted() {
    consumer.append("hello");
    consumer.endStatement();
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);

    consumer.append(" world");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
    assertTrue(consumer.statementStarted);
  }

  @org.junit.Test
  public void testSawFunction() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
  }

  @Override
  void append(String str) {
    super.append(str);
  }

  @Override
  void appendBlockStart() {
    super.appendBlockStart();
  }

  @Override
  void appendBlockEnd() {
    super.appendBlockEnd();
  }

  @Override
  void startNewLine() {
    super.startNewLine();
  }

  @Override
  void maybeLineBreak() {
    super.maybeLineBreak();
  }

  @Override
  void maybeCutLine() {
    super.maybeCutLine();
  }

  @Override
  void endLine() {
    super.endLine();
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
  }

  @Override
  void beginBlock() {
    super.beginBlock();
  }

  @Override
  void endBlock() {
    super.endBlock();
  }

  @Override
  void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
  }

  @Override
  void listSeparator() {
    super.listSeparator();
  }

  @Override
  void endStatement() {
    super.endStatement();
  }

  @Override
  void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
  }

  @Override
  void maybeEndStatement() {
    super.maybeEndStatement();
  }

  @Override
  void endFunction() {
    super.endFunction();
  }

  @Override
  void endFunction(boolean statementContext) {
    super.endFunction(statementContext);
  }

  @Override
  void beginCaseBody() {
    super.beginCaseBody();
  }

  @Override
  void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return super.shouldPreserveExtraBlocks();
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return super.breakAfterBlockFor(n, statementContext);
  }

  @Override
  void endFile() {
    super.endFile();
  }
}