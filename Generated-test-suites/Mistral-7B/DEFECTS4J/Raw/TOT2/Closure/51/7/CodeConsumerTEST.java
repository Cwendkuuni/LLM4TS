import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

 private CodeConsumer consumer = new TestCodeConsumer();

 @Test
 public void testStartSourceMapping() {
 Node node = Node.newIntLiteral(1);
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 // Check that no error was thrown
 }

 @Test
 public void testContinueProcessingReturnsTrueByDefault() {
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testContinueProcessingReturnsFalseWhenOverrided() {
 consumer = new TestCodeConsumerForContinueProcessingFalse();
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastCharReturnsLastCharacterAppended() {
 consumer.append("Hello");
 assertEquals('H', consumer.getLastChar());
 }

 @Test
 public void testAppendString() {
 consumer.append("Hello");
 assertEquals("Hello", consumer.getCode());
 }

 @Test
 public void testAppendBlockStartAndEnd() {
 consumer.beginBlock();
 consumer.append("Hello");
 consumer.endBlock();
 assertEquals("Hello{\n}", consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals("\n", consumer.getCode().substring(consumer.getCode().length() - 1));
 }

 @Test
 public void testAppendIdentifier() {
 consumer.addIdentifier("foo");
 assertEquals("foo", consumer.getCode().substring(consumer.getCode().lastIndexOf("foo") + 3));
 }

 @Test
 public void testAppendBlockWithStatementNeedsToEnd() {
 consumer.appendBlockStart();
 consumer.endStatement();
 consumer.append("Hello");
 consumer.endBlock();
 assertEquals("Hello{\n}", consumer.getCode());
 }

 @Test
 public void testAppendBlockWithStatementDoesNotNeedToEnd() {
 consumer.appendBlockStart();
 consumer.append("Hello");
 consumer.appendBlockEnd();
 assertEquals("Hello{\n}", consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testEndBlockWithoutBeginBlock() {
 consumer.endBlock();
 }

 @Test
 public void testEndFunction() {
 consumer.beginBlock();
 consumer.append("function foo() {");
 consumer.append("}");
 consumer.endFunction();
 assertEquals("function foo() {\n}\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.beginBlock();
 consumer.append("function foo() {\nreturn 1;\n}");
 consumer.endFunction(true);
 assertEquals("function foo() {\n  return 1;\n}\n", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(-1);
 assertEquals("-1\n", consumer.getCode());
 consumer.addNumber(1.5);
 assertEquals("1.5\n", consumer.getCode());
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('0'));
 assertTrue(CodeConsumer.isWordChar('A'));
 }

 @Test
 public void testShouldPreserveExtraBlocksReturnsFalseByDefault() {
 assertEquals(false, consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = Node.newLiteral("return");
 assertTrue(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForWithStatementContext() {
 Node node = Node.newLiteral("return");
 assertFalse(consumer.breakAfterBlockFor(node, true));
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }
}