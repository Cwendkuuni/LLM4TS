import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 consumer.startSourceMapping(createNode("1"));
 consumer.endSourceMapping(createNode("2"));
 // Verify that nothing was appended to the output
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testContinueProcessing() {
 consumer.statementNeedsEnded = true;
 assertFalse(consumer.continueProcessing());
 consumer.statementNeedsEnded = false;
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 consumer.append("abc");
 assertEquals('c', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getOutput());
 }

 @Test
 public void testAppend() {
 consumer.append("string");
 assertEquals("string", consumer.getOutput());
 }

 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 assertEquals("{\n", consumer.getOutput());
 }

 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 assertEquals("}\n", consumer.getOutput());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals("\n", consumer.getOutput());
 }

 @Test
 public void testMaybeLineBreak() {
 consumer.maybeLineBreak();
 // Verify that nothing was appended to the output
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 // Verify that nothing was appended to the output
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getOutput());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 // Verify that nothing was appended to the output
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getOutput());
 }

 @Test
 public void testEndBlock() {
 consumer.endBlock();
 assertEquals("}\n", consumer.getOutput());
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 assertEquals(",\n", consumer.getOutput());
 }

 @Test
 public void testEndStatement() {
 consumer.endStatement();
 assertEquals(";\n", consumer.getOutput());
 }

 @Test
 public void testEndStatementWithSemiColon() {
 consumer.endStatement(true);
 assertEquals(";\n", consumer.getOutput());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.statementNeedsEnded = true;
 consumer.maybeEndStatement();
 assertEquals(";\n", consumer.getOutput());
 consumer.statementNeedsEnded = false;
 consumer.statementStarted = true;
 consumer.maybeEndStatement();
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 assertEquals("}\n", consumer.getOutput());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.endFunction(true);
 assertEquals("\n}", consumer.getOutput());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getOutput());
 }

 @Test
 public void testEndCaseBody() {
 consumer.endCaseBody();
 // Verify that nothing was appended to the output
 assertEquals("", consumer.getOutput());
 }

 @Test
 public void testAdd() {
 consumer.add("string");
 assertEquals("string", consumer.getOutput());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", false);
 assertEquals("+", consumer.getOutput());
 consumer.addOp("+", true);
 assertEquals("+\n", consumer.getOutput());
 consumer.addOp("++", false);
 assertEquals("++", consumer.getOutput());
 consumer.addOp("++", true);
 assertEquals("++\n", consumer.getOutput());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(123.456);
 assertEquals("123.456", consumer.getOutput());
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('0'));
 assertFalse(CodeConsumer.isWordChar('z'));
 assertFalse(CodeConsumer.isWordChar('!'));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = createNode("if (true) {}");
 assertTrue(consumer.breakAfterBlockFor(node, false));
 assertFalse(consumer.breakAfterBlockFor(node, true));
 node = createNode("for (int i = 0; i < 10; i++) {}", false, 0);
 assertTrue(consumer.breakAfterBlockFor(node, false));
 assertFalse(consumer.breakAfterBlockFor(node, true));
 }

 @Test(expected = AssertionError.class)
 public void testEndFile() {
 consumer.endFile();
 }

 private Node createNode(String code, boolean isStatement, int lineNumber) {
 Node node = new Node();
 node.setLineNumber(lineNumber);
 Node body = new Node_();
 body.setType(Node.ABSTRACT);
 body.setClassName("Javascript$Statement");
 node.setBody(body);
 node.setCode(code);
 node.setIsStatement(isStatement);
 return node;
 }

 private static class Node_ extends Node {
 }

}