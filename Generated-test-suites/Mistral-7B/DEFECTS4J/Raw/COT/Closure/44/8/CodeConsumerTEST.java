import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new StubCodeConsumer();
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
 consumer = new CodeConsumer() {
 @Override
 public boolean continueProcessing() {
 return false;
 }
 };
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 String code = "hello";
 consumer = new CodeConsumer() {
 @Override
 char getLastChar() {
 return code.charAt(code.length() - 1);
 }
 };
 assertEquals('o', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("test");
 assertEquals("test", consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("hello");
 assertEquals("hello", consumer.getCode());
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
 consumer.maybeLineBreak();
 consumer.append("world");
 assertEquals("\nworld", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 consumer.append("world");
 assertEquals("world\n", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 consumer.append("world");
 assertEquals("\nworld", consumer.getCode());
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
 public void testEndBlockWithNewline() {
 consumer.endBlock(true);
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
 consumer.append("world");
 assertEquals("world\n", consumer.getCode());
 }

 @Test
 public void testEndStatementWithSemiColon() {
 consumer.endStatement(true);
 consumer.append("world");
 assertEquals(";\nworld\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.maybeEndStatement();
 consumer.append("world");
 assertEquals("world", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatementWithSemiColon() {
 consumer.maybeEndStatement(true);
 consumer.append("world");
 assertEquals("world;\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.endFunction(true);
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
 consumer.add("1 + 2");
 assertEquals("1 + 2\n", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", false);
 consumer.append("1");
 consumer.addOp("+", false);
 consumer.append("2");
 assertEquals("1 + 2\n", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(123.456);
 assertEquals("123.456\n", consumer.getCode());
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 assertTrue(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForWithStatementContext() {
 Node node = new Node();
 assertFalse(consumer.breakAfterBlockFor(node, true));
 }

 @Test(expected = AssertionError.class)
 public void testEndFile() {
 consumer.endFile();
 fail("Should not reach this point");
 }
}

class StubCodeConsumer extends CodeConsumer {
 private StringBuilder code = new StringBuilder();

 @Override
 void append(String str) {
 code.append(str);
 }

 @Override
 void startNewLine() {
 code.append("\n");
 }

 @Override
 char getLastChar() {
 return code.charAt(code.length() - 1);
 }

 @Override
 String getCode() {
 return code.toString();
 }
}