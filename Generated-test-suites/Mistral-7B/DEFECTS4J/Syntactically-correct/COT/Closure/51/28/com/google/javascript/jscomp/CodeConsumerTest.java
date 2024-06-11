import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setup() {
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
 consumer = new TestCodeConsumerWithContinueProcessingFalse();
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 // Not testing this method directly as it's abstract and we can't create an instance of the abstract class
 // To test this method, create a concrete subclass and test it there
 }

 @Test
 public void testAppend() {
 consumer.append("testString");
 String expected = "testString";
 assertEquals(expected, consumer.getCode());
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
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 assertEquals("", consumer.getCode());
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
 public void testEndStatementWithSemiColon() {
 consumer.endStatement(true);
 assertEquals(";\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.maybeEndStatement();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatementWithSemiColon() {
 consumer.maybeEndStatement(true);
 assertEquals(";\n", consumer.getCode());
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
 endLine();
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
 consumer.add("testIdentifier");
 assertEquals("testIdentifier\n", consumer.getCode());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("testIdentifier");
 assertEquals("testIdentifier\n", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", false);
 assertEquals("+\n", consumer.getCode());
 }

 @Test
 public void testAddOpWithBinary() {
 consumer.addOp("*", true);
 assertEquals("*\n", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(123.45);
 assertEquals("123.45\n", consumer.getCode());
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('1'));
 assertFalse(CodeConsumer.isWordChar('!'));
 assertFalse(CodeConsumer.isWordChar('.'));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockForTrue() {
 assertTrue(consumer.breakAfterBlockFor(null, true));
 }

 @Test
 public void testBreakAfterBlockForFalse() {
 assertFalse(consumer.breakAfterBlockFor(null, false));
 }

 @Test(expected = AssertionError.class)
 public void testAddWithMalformedCode() {
 consumer.add("badCode");
 }
}

class TestCodeConsumer extends CodeConsumer {
 String code = "";

 @Override
 void append(String str) {
 code += str;
 }

 @Override
 char getLastChar() {
 return code.charAt(code.length() - 1);
 }

 @Override
 String getCode() {
 return code;
 }
}

class TestCodeConsumerWithContinueProcessingFalse extends TestCodeConsumer {
 @Override
 boolean continueProcessing() {
 return false;
 }
}