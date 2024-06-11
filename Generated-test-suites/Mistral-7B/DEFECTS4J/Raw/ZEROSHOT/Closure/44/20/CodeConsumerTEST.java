import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert anything here as startSourceMapping is empty
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert anything here as endSourceMapping is empty
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("testIdentifier");
    String expected = "testIdentifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test string");
    String expected = "test string";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // Cannot assert anything here as maybeLineBreak is empty
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // Cannot assert anything here as maybeCutLine is empty
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot assert anything here as notePreferredLineBreak is empty
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    String expected = "{\ntest\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test, ");
    String expected = "test,\n ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    String expected = "test;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expected = "test";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {}");
    consumer.endFunction();
    String expected = "{\nfunction test() {\n}\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() { return 5; }");
    consumer.endFunction(true);
    String expected = "{\nfunction test() {\n  return 5;\n}\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (x) { case 1: }");
    consumer.beginCaseBody();
    String expected = "switch (x) {\n case 1:\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (x) { case 1: }");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expected = "switch (x) {\n case 1:\ntest\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.add(" string");
    String expected = "test string";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("5");
    String expected = "test + 5";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addNumber(5.5);
    String expected = "test 5.5";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForSimpleStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForBlockStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    Node blockNode = new Node();
    node.setFirstChild(blockNode);
    blockNode.setType(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForIfStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    Node ifNode = new Node();
    Node conditionNode = new Node();
    Node bodyNode = new Node();
    node.setFirstChild(ifNode);
    ifNode.setType(Node.IF_STMT);
    ifNode.setFirstChild(conditionNode);
    conditionNode.setType(Node.LOGICAL_OR_EXPRESSION);
    conditionNode.setFirstChild(new Node("true"));
    conditionNode.setLastChild(new Node("false"));
    ifNode.setLastChild(bodyNode);
    bodyNode.setType(Node.BLOCK);
    bodyNode.setFirstChild(new Node("test"));
    bodyNode.setLastChild(new Node("test"));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForForStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    Node forNode = new Node();
    Node initNode = new Node();
    Node conditionNode = new Node();
    Node incrementNode = new Node();
    Node bodyNode = new Node();
    node.setFirstChild(forNode);
    forNode.setType(Node.FOR_STMT);
    forNode.setFirstChild(initNode);
    initNode.setType(Node.VAR_STMT);
    initNode.setFirstChild(new Node("int i = 0;"));
    forNode.setLastChild(incrementNode);
    incrementNode.setType(Node.EXPRESSION_STATEMENT);
    incrementNode.setFirstChild(new Node("i++"));
    forNode.setLastChild(bodyNode);
    bodyNode.setType(Node.BLOCK);
    bodyNode.setFirstChild(new Node("test"));
    bodyNode.setLastChild(new Node("test"));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // Cannot assert anything here as endFile is empty
  }

}

class StubCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return ' ';
  }

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
  public void endBlock() {
    super.endBlock();
  }

  @Override
  public void endFunction() {
    super.endFunction();
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
  public String getCode() {
    return "";
  }

  @Override
  public void addIdentifier(String identifier) {
    super.addIdentifier(identifier);
  }

  @Override
  public void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  public boolean shouldPreserveExtraBlocks() {
    return super.shouldPreserveExtraBlocks();
  }

  @Override
  public boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return super.breakAfterBlockFor(n, statementContext);
  }

  @Override
  public void endFile() {
    super.endFile();
  }
}