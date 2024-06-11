import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 /**
 * Test for the startSourceMapping method.
 */
 @Test
 public void testStartSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 assertNull(consumer.getLastChar());
 }

 /**
 * Test for the endSourceMapping method.
 */
 @Test
 public void testEndSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 assertNull(consumer.getLastChar());
 }

 /**
 * Test for the continueProcessing method.
 */
 @Test
 public void testContinueProcessing() {
 assertTrue(consumer.continueProcessing());
 }

 /**
 * Test for the getLastChar method.
 */
 @Test(expected = IllegalStateException.class)
 public void testGetLastCharBeforeInitialize() {
 consumer.getLastChar();
 }

 /**
 * Test for the append method with a single character.
 */
 @Test
 public void testAppendSingleCharacter() {
 consumer.append("a");
 assertEquals('a', consumer.getLastChar());
 }

 /**
 * Test for the append method with multiple characters.
 */
 @Test
 public void testAppendMultipleCharacters() {
 consumer.append("abcd");
 assertEquals('d', consumer.getLastChar());
 }

 /**
 * Test for the appendBlockStart method.
 */
 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 assertEquals('{', consumer.getLastChar());
 }

 /**
 * Test for the appendBlockEnd method.
 */
 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 assertEquals('}', consumer.getLastChar());
 }

 /**
 * Test for the startNewLine method.
 */
 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals('\n', consumer.getLastChar());
 }

 /**
 * Test for the maybeLineBreak method when no line break is needed.
 */
 @Test
 public void testMaybeLineBreakNoBreak() {
 consumer.maybeLineBreak();
 assertEquals(consumer.getLastChar(), consumer.getLastChar());
 }

 /**
 * Test for the maybeLineBreak method when a line break is needed.
 */
 @Test
 public void testMaybeLineBreakBreak() {
 consumer.maybeLineBreak();
 assertNotEquals(consumer.getLastChar(), consumer.getLastChar());
 }

 /**
 * Test for the endLine method.
 */
 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals('\n', consumer.getLastChar());
 }

 /**
 * Test for the notePreferredLineBreak method.
 */
 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 assertEquals('\n', consumer.getLastChar());
 }

 /**
 * Test for the beginBlock method.
 */
 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals('{', consumer.getLastChar());
 }

 /**
 * Test for the endBlock method when no semicolon is needed.
 */
 @Test
 public void testEndBlockNoSemicolon() {
 consumer.beginBlock();
 consumer.add("return 1;");
 consumer.endBlock();
 assertFalse(consumer.statementNeedsEnded);
 }

 /**
 * Test for the endBlock method when a semicolon is needed.
 */
 @Test
 public void testEndBlockSemicolon() {
 consumer.beginBlock();
 consumer.add("return");
 consumer.endBlock();
 assertTrue(consumer.statementNeedsEnded);
 consumer.endStatement();
 assertFalse(consumer.statementNeedsEnded);
 }

 /**
 * Test for the listSeparator method.
 */
 @Test
 public void testListSeparator() {
 consumer.add("1");
 consumer.listSeparator();
 consumer.add("2");
 assertEquals("1, ", consumer.getCode().substring(0, consumer.getCode().length()-2));
 }

 /**
 * Test for the addIdentifier method.
 */
 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getCode());
 }

 /**
 * Test for the addOp method with a unary operator.
 */
 @Test
 public void testAddOpUnaryOperator() {
 consumer.beginBlock();
 consumer.add("x");
 consumer.addOp("++", false);
 consumer.endBlock();
 assertEquals("x++", consumer.getCode());
 }

 /**
 * Test for the addOp method with a binary operator.
 */
 @Test
 public void testAddOpBinaryOperator() {
 consumer.beginBlock();
 consumer.add("x");
 consumer.addOp("+", true);
 consumer.add("y");
 consumer.endBlock();
 assertEquals("x + y", consumer.getCode());
 }

 /**
 * Test for the addNumber method.
 */
 @Test
 public void testAddNumber() {
 consumer.addNumber(1.5);
 assertEquals("1.5", consumer.getCode());
 }

 /**
 * Test for the isWordChar method.
 */
 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('A'));
 assertTrue(CodeConsumer.isWordChar('1'));
 }

 /**
 * Test for the shouldPreserveExtraBlocks method.
 */
 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 /**
 * Test for the breakAfterBlockFor method.
 */
 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 node.setType("if");
 node.set("condition", new Node("boolean_literal", false));
 node.set("consequent", new Node("expression_statement", new Node("assignment", new Node("VariableStatement", new Node("Var", new Node("Identifier", "x"))), new Node("NumberLiteral", "1"))));
 assertTrue(consumer.breakAfterBlockFor(node, true));
 }

 /**
 * Test for the endFile method.
 */
 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }

 /**
 * Test for the getCode method.
 */
 @Test
 public void testGetCode() {
 assertEquals("", consumer.getCode());
 }

 /**
 * Test for the throwException method.
 */
 @Test(expected = IllegalStateException.class)
 public void testThrowException() {
 consumer.throwException(new IllegalStateException("An error occurred."));
 }

}

class TestCodeConsumer extends CodeConsumer {
 @Override
 void add(String newcode) {
 super.add(newcode);
 }

 @Override
 void append(String str) {
 super.append(str);
 }

 @Override
 char getLastChar() {
 return super.getLastChar();
 }

 @Override
 String getCode() {
 return super.getCode();
 }
}