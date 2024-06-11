import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new StubCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 consumer.startSourceMapping(new Node());
 // Nothing to assert
 }

 @Test
 public void testEndSourceMapping() {
 consumer.endSourceMapping(new Node());
 // Nothing to assert
 }

 @Test
 public void testContinueProcessing() {
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 // Cannot test directly as abstract method
 }

 @Test
 public void testAppend() {
 String testString = "testString";
 consumer.append(testString);
 assertEquals(testString, consumer.getCode());
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
 // Cannot test directly as abstract method
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 // Nothing to assert
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
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 assertEquals("}\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.endFunction(true);
 assertEquals("\n}", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 // Nothing to assert
 }

 @Test
 public void testAdd() {
 String testString = "testString";
 consumer.add(testString);
 assertEquals(testString, consumer.getCode().substring(0, testString.length()));
 }

 @Test
 public void testAddOp() {
 String testStringPlus = "+";
 String testStringMinus = "-";
 String testStringMultiply = "*";
 String testStringDivide = "/";
 String testStringEqual = "=";
 String testStringNotEqual = "!=";
 String testStringLessThan = "<";
 String testStringGreaterThan = ">";
 String testStringLessThanOrEqual = "<=";
 String testStringGreaterThanOrEqual = ">=";

 consumer.addOp(testStringPlus, false);
 assertEquals(testStringPlus, consumer.getCode().substring(0, testStringPlus.length()));
 consumer.addOp(testStringMinus, false);
 assertEquals(testStringMinus, consumer.getCode().substring(0, testStringMinus.length()));
 consumer.addOp(testStringMultiply, true);
 assertEquals(testStringMultiply, consumer.getCode().substring(0, testStringMultiply.length()));
 consumer.addOp(testStringDivide, true);
 assertEquals(testStringDivide, consumer.getCode().substring(0, testStringDivide.length()));
 consumer.addOp(testStringEqual, false);
 assertEquals(testStringEqual, consumer.getCode().substring(0, testStringEqual.length()));
 consumer.addOp(testStringNotEqual, false);
 assertEquals(testStringNotEqual, consumer.getCode().substring(0, testStringNotEqual.length()));
 consumer.addOp(testStringLessThan, false);
 assertEquals(testStringLessThan, consumer.getCode().substring(0, testStringLessThan.length()));
 consumer.addOp(testStringGreaterThan, false);
 assertEquals(testStringGreaterThan, consumer.getCode().substring(0, testStringGreaterThan.length()));
 consumer.addOp(testStringLessThanOrEqual, false);
 assertEquals(testStringLessThanOrEqual, consumer.getCode().substring(0, testStringLessThanOrEqual.length()));
 consumer.addOp(testStringGreaterThanOrEqual, false);
 assertEquals(testStringGreaterThanOrEqual, consumer.getCode().substring(0, testStringGreaterThanOrEqual.length()));
 }

 @Test
 public void testAddNumber() {
 double testNumber = 123.456;
 consumer.addNumber(testNumber);
 assertEquals(String.valueOf(testNumber), consumer.getCode());
 }

 @Test
 public void testIsNegativeZero() {
 double testNumber = 0.0;
 assertTrue(CodeConsumer.isNegativeZero(testNumber));
 }

 @Test
 public void testIsWordChar() {
 char testChar1 = '_';
 char testChar2 = '$';
 char testChar3 = 'A';
 char testChar4 = 'Z';
 char testChar5 = '0';
 char testChar6 = '9';
 assertTrue(CodeConsumer.isWordChar(testChar1));
 assertTrue(CodeConsumer.isWordChar(testChar2));
 assertTrue(CodeConsumer.isWordChar(testChar3));
 assertTrue(CodeConsumer.isWordChar(testChar4));
 assertTrue(CodeConsumer.isWordChar(testChar5));
 assertTrue(CodeConsumer.isWordChar(testChar6));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertFalse(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 assertFalse(consumer.breakAfterBlockFor(new Node(), false));
 assertTrue(consumer.breakAfterBlockFor(new Node(), true));
 }

 @Test
 public void testEndFile() {
 // Nothing to assert
 }

}

class StubCodeConsumer extends CodeConsumer {
 char getLastChar() {
 return ' ';
 }
 String getCode() {
 return "";
 }
}