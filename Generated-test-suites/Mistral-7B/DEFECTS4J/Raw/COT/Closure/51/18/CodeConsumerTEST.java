import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 consumer.startSourceMapping(null);
 }

 @Test
 public void testEndSourceMapping() {
 consumer.endSourceMapping(null);
 }

 @Test
 public void testContinueProcessing() {
 assertTrue(consumer.continueProcessing());
 }

 @Test(expected = AssertionError.class)
 public void testContinueProcessingReturnsFalse() {
 consumer = new TestCodeConsumerForContinueProcessingFalse();
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 assertEquals(' ', consumer.getLastChar());
 consumer.append("abcd");
 assertEquals('d', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("string");
 assertEquals("string", consumer.getCode());
 }

 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeLineBreak() {
 consumer.addIdentifier("identifier1");
 consumer.maybeLineBreak();
 consumer.addIdentifier("identifier2");
 assertEquals("identifier1\nidentifier2", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.append("abcd");
 consumer.maybeCutLine();
 consumer.append("efgh");
 assertEquals("abcd\nefgh", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.endBlock();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 assertEquals(",\n", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.endStatement();
 assertEquals(";\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.addIdentifier("identifier1");
 consumer.maybeEndStatement();
 consumer.addIdentifier("identifier2");
 assertEquals("identifier1;\nidentifier2", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 consumer.endCaseBody();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testAdd() {
 consumer.add("string");
 assertEquals("string", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", false);
 assertEquals(" + ", consumer.getCode());
 consumer.addOp("+=", false);
 assertEquals(" += ", consumer.getCode());
 consumer.addOp("+", true);
 assertEquals(" + ", consumer.getCode());
 consumer.addOp("+=", true);
 assertEquals(" += ", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(1.23);
 assertEquals("1.23", consumer.getCode());
 consumer.addNumber(-1.23);
 assertEquals("-1.23", consumer.getCode());
 consumer.addNumber(123);
 assertEquals("123", consumer.getCode());
 consumer.addNumber(-123);
 assertEquals("-123", consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testIsWordCharInvalidChar() {
 assertFalse(CodeConsumer.isWordChar('@'));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockForFalse() {
 consumer = new TestCodeConsumerForBreakAfterBlockForFalse();
 Node node = new Node_(); // Dummy Node
 assertFalse(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForTrue() {
 consumer = new TestCodeConsumerForBreakAfterBlockForTrue();
 Node node = new Node_(); // Dummy Node
 assertTrue(consumer.breakAfterBlockFor(node, true));
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }

 private class TestCodeConsumer extends CodeConsumer {
 StringBuilder code = new StringBuilder();
 @Override
 public void append(String str) {
 code.append(str);
 }

 @Override
 public char getLastChar() {
 return code.charAt(code.length() - 1);
 }

 @Override
 public String getCode() {
 return code.toString();
 }
 }

 private static class TestCodeConsumerForContinueProcessingFalse extends TestCodeConsumer {
 @Override
 public boolean continueProcessing() {
 return false;
 }
 }

 private static class TestCodeConsumerForBreakAfterBlockForFalse extends TestCodeConsumer {
 @Override
 public boolean breakAfterBlockFor(Node n, boolean statementContext) {
 return false;
 }
 }

 private static class TestCodeConsumerForBreakAfterBlockForTrue extends TestCodeConsumer {
 @Override
 public boolean breakAfterBlockFor(Node n, boolean statementContext) {
 return true;
 }
 }
}