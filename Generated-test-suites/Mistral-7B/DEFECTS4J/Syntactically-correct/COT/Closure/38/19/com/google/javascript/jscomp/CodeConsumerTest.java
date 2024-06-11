import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

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

 @Test
 public void testGetLastChar() {
 assertEquals('\u0000', consumer.getLastChar());
 consumer.append("abc");
 assertEquals('c', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("abc");
 assertEquals("abc", consumer.getCode());
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
 consumer.append("ab");
 consumer.maybeLineBreak();
 consumer.append("cd");
 assertEquals("ab\ncd", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.append("abc");
 consumer.maybeCutLine();
 consumer.append("def");
 assertEquals("abc\ndef", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.append("ab");
 consumer.endLine();
 assertEquals("ab\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.append("ab");
 consumer.notePreferredLineBreak();
 assertEquals("ab\n", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.append("ab");
 consumer.beginBlock();
 consumer.append("cd");
 consumer.endBlock();
 assertEquals("ab{\ncd\n}", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.append("ab");
 consumer.listSeparator();
 consumer.append("cd");
 assertEquals("ab,\ncd", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.append("if (true) {\n}");
 consumer.endStatement();
 consumer.append("else {\n}");
 consumer.endStatement();
 consumer.append(";");
 consumer.endStatement();
 consumer.append(";");
 consumer.endStatement();
 assertEquals("if (true) {\n}\nelse {\n}\nab; \n\nab;", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.append("function foo() {\n}");
 consumer.endFunction();
 consumer.append("function bar() {\n}");
 consumer.endFunction();
 consumer.append(";");
 consumer.endFunction();
 consumer.append(";");
 consumer.endFunction();
 assertEquals("function foo() {\n}\nfunction bar() {\n}\n; \n\n;", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.append("switch (x) {\ncase 1:\n}");
 consumer.beginCaseBody();
 consumer.append("alert(\"one\");");
 consumer.endCaseBody();
 consumer.append("break;\ncase 2:\n}");
 consumer.beginCaseBody();
 consumer.append("alert(\"two\");");
 consumer.endCaseBody();
 consumer.endStatement();
 assertEquals("switch (x) {\ncase 1:\nalert(\"one\");\nbreak;\ncase 2:\nalert(\"two\");\n}", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("5 + 3 * 2");
 consumer.addOp("+", false);
 consumer.addOp("*", true);
 consumer.addOp("+", false);
 assertEquals("5 + (3 * 2)", consumer.getCode());
 }

 @Test (expected = AssertionError.class)
 public void testAddNumberWithTooLongDouble() {
 consumer.append("1.23456789012345678901234567890E5");
 consumer.addNumber(1.23456789012345678901234567890E5);
 fail("Should not be able to add number that long.");
 }

 @Test (expected = AssertionError.class)
 public void testAddNumberWithNegativeZero() {
 consumer.append("5 + -0");
 consumer.addNumber(0.0);
 fail("Should not be able to add negative zero.");
 }

 @Test
 public void testIsNegativeZero() {
 assertEquals(false, CodeConsumer.isNegativeZero(-0.0));
 assertEquals(false, CodeConsumer.isNegativeZero(0.0));
 assertEquals(true, CodeConsumer.isNegativeZero(-0.0 + 1.0));
 }

 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('0'));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node_();
 assertTrue(consumer.breakAfterBlockFor(node, true));
 assertFalse(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testEndFile() {
 consumer.append("// this is a comment");
 consumer.endFile();
 assertEquals("// this is a comment\n", consumer.getCode());
 }

 @Test
 public void testGetCode() {
 assertEquals("", consumer.getCode());
 }

}

class TestCodeConsumer extends CodeConsumer {
 @Override
 public char getLastChar() {
 return super.getLastChar();
 }

 @Override
 public void append(String str) {
 super.append(str);
 }

 @Override
 public void add(String newcode) {
 super.add(newcode);
 }
}