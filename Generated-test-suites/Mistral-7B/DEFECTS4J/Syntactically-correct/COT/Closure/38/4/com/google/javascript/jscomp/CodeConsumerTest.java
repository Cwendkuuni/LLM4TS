import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

 abstract static class TestCodeConsumer extends CodeConsumer {
 char lastChar;

 @Override
 char getLastChar() {
 return lastChar;
 }

 @Override
 void add(String newcode) {
 super.add(newcode);
 lastChar = newcode.charAt(newcode.length() - 1);
 }
 }

 @Test
 public void testStartSourceMapping() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 consumer.startSourceMapping(node);
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testEndSourceMapping() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testContinueProcessing() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.append("hello");
 assertEquals('l', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.addIdentifier("foo");
 assertEquals('f', consumer.getLastChar());
 }

 @Test
 public void testAppend() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.append("hello");
 assertEquals('o', consumer.getLastChar());
 }

 @Test
 public void testAppendBlockStart() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.appendBlockStart();
 assertEquals('{', consumer.getLastChar());
 }

 @Test
 public void testAppendBlockEnd() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.appendBlockEnd();
 assertEquals('}', consumer.getLastChar());
 }

 @Test
 public void testStartNewLine() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.startNewLine();
 assertEquals('\n', consumer.getLastChar());
 }

 @Test
 public void testMaybeLineBreak() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.maybeLineBreak();
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testMaybeCutLine() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.maybeCutLine();
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testEndLine() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endLine();
 assertEquals('\n', consumer.getLastChar());
 }

 @Test
 public void testNotePreferredLineBreak() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.notePreferredLineBreak();
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testBeginBlock() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.beginBlock();
 assertEquals('{', consumer.getLastChar());
 }

 @Test
 public void testEndBlock() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endBlock();
 assertEquals('}', consumer.getLastChar());
 }

 @Test
 public void testEndBlockWithNewline() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endBlock(true);
 assertEquals('\n', consumer.getLastChar());
 }

 @Test
 public void testListSeparator() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.listSeparator();
 assertEquals(',', consumer.getLastChar());
 }

 @Test
 public void testEndStatement() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endStatement();
 assertEquals(';', consumer.getLastChar());
 }

 @Test
 public void testEndStatementWithNewline() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endStatement(true);
 assertEquals('\n', consumer.getLastChar());
 }

 @Test
 public void testMaybeEndStatement() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.maybeEndStatement();
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testEndFunction() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endFunction();
 assertFalse(consumer.sawFunction);
 }

 @Test
 public void testEndFunctionWithNewline() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endFunction(true);
 assertTrue(consumer.sawFunction);
 }

 @Test
 public void testBeginCaseBody() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.beginCaseBody();
 assertEquals(':', consumer.getLastChar());
 }

 @Test
 public void testEndCaseBody() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endCaseBody();
 assertNull(consumer.getLastChar());
 }

 @Test
 public void testAdd() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.add("hello");
 assertEquals('o', consumer.getLastChar());
 }

 @Test
 public void testAddOp() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.addOp("+", false);
 assertEquals('+', consumer.getLastChar());
 consumer.addOp("+", true);
 assertEquals(" + ", consumer.getLastCode());
 }

 @Test
 public void testAddNumber() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.addNumber(123);
 assertEquals('3', consumer.getLastChar());
 consumer.addNumber(123.45);
 assertEquals('5', consumer.getLastChar());
 consumer.addNumber(-123);
 assertEquals('3', consumer.getLastChar());
 consumer.addNumber(-123.45);
 assertEquals('5', consumer.getLastChar());
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 assertFalse(consumer.shouldPreserveExtraBlocks());
 }

 @Test(expected = AssertionError.class)
 public void testBreakAfterBlockForNodeWithNoStatements() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 boolean result = consumer.breakAfterBlockFor(node, false);
 fail("Expected false, but got " + result);
 }

 @Test
 public void testBreakAfterBlockForNodeWithSingleStatement() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 node.setType("ExpressionStatement");
 Node statement = new Node();
 statement.setValue("42");
 node.setFirstChild(statement);
 assertTrue(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForNodeWithMultipleStatements() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 Node statement1 = new Node();
 statement1.setValue("42");
 Node statement2 = new Node();
 statement2.setValue("43");
 node.setFirstChild(statement1);
 node.setNext(statement2);
 assertFalse(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForNodeWithStatementContext() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 Node node = new Node();
 Node statement = new Node();
 statement.setValue("42");
 node.setType("IfStatement");
 node.setFirstChild(statement);
 assertTrue(consumer.breakAfterBlockFor(node, true));
 }

 @Test
 public void testEndFile() {
 TestCodeConsumer consumer = new TestCodeConsumer();
 consumer.endFile();
 assertNull(consumer.getLastChar());
 }
}