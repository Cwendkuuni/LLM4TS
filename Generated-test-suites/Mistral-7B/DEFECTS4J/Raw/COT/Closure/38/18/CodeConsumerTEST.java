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

 @Test
 public void testGetLastChar() {
 assertNull(consumer.getLastChar());
 consumer.append("abc");
 assertEquals('a', consumer.getLastChar());
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
 assertFalse(consumer.getCode().isEmpty());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 assertFalse(consumer.getCode().isEmpty());
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
 public void testEndBlockWithLineBreak() {
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
 assertEquals(";\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatementWithoutSemiColon() {
 consumer.maybeEndStatement();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 assertEquals("}", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithLineBreak() {
 consumer.endFunction(true);
 assertEquals("}\n", consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testEndFunctionWithoutStatementContextThrowsError() {
 consumer.endFunction(false);
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
 consumer.add("test");
 assertEquals("test", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+");
 assertEquals(" +", consumer.getCode());
 }

 @Test
 public void testAddOpWithBinary() {
 consumer.addOp("==", true);
 assertEquals(" ==", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(1.0);
 assertEquals("1.0\n", consumer.getCode());
 consumer.addNumber(-1.0);
 assertEquals("-1.0\n", consumer.getCode());
 consumer.addNumber(0.0);
 assertEquals("0.0\n", consumer.getCode());
 consumer.addNumber(123.456);
 assertEquals("123.456\n", consumer.getCode());
 consumer.addNumber(-123.456);
 assertEquals("-123.456\n", consumer.getCode());
 consumer.addNumber(-0.0);
 assertEquals("-0.0\n", consumer.getCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNumberNonNumberThrowsError() {
 consumer.addNumber("not a number");
 }

 @Test
 public void testIsNegativeZero() {
 assertTrue(CodeConsumer.isNegativeZero(-0.0));
 assertFalse(CodeConsumer.isNegativeZero(0.0));
 assertFalse(CodeConsumer.isNegativeZero(1.0));
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(Character.isLetterOrDigit('a'));
 assertTrue(CodeConsumer.isWordChar('x'));
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

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testSawFunction() {
 consumer.endFunction();
 consumer.endFunction();
 assertTrue(consumer.sawFunction);
 }

}

 class TestCodeConsumer extends CodeConsumer {
 @Override
 void beginBlock() {
 super.beginBlock();
 add("test_begin_block");
 }

 @Override
 void endBlock() {
 super.endBlock();
 add("test_end_block");
 }

 @Override
 void add(String newcode) {
 super.add(newcode);
 add("_test_" + newcode);
 }

 @Override
 void addOp(String op, boolean binOp) {
 super.addOp(op, binOp);
 add("_op_" + op);
 }

 @Override
 void addNumber(double x) {
 super.addNumber(x);
 add("_number_" + x);
 }

 @Override
 String getCode() {
 String code = super.getCode();
 return code.substring(0, code.length() - 1) + "_test_";
 }
 }