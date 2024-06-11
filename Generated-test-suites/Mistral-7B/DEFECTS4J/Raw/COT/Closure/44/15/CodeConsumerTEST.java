import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 consumer.startSourceMapping(newNode("1"));
 consumer.endSourceMapping(newNode("1"));
 }

 @Test
 public void testContinueProcessing() {
 consumer.continuueProcessing();
 assertTrue(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 consumer.append("foo");
 assertEquals('f', consumer.getLastChar());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 assertEquals("identifier", consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("foo");
 assertEquals("foo", consumer.getCode());
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
 consumer.append("foo");
 consumer.maybeLineBreak();
 consumer.append("bar");
 assertEquals("foo\nbar", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.append("foo");
 consumer.maybeCutLine();
 consumer.append("bar");
 assertEquals("foo\nbAR", consumer.getCode());
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
 consumer.append("foo");
 consumer.endStatement();
 consumer.append("bar");
 assertEquals("foo;\nbar", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.append("function foo() {");
 consumer.append("}");
 consumer.endFunction();
 consumer.append("bar");
 assertEquals("function foo() {\n}\nbar", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 consumer.append("case 1:");
 consumer.endCaseBody();
 assertEquals("case 1:\n", consumer.getCode());
 }

 @Test
 public void testAdd() {
 consumer.append("foo");
 consumer.add("bar");
 assertEquals("foobar", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("foo");
 consumer.addOp("+", true);
 consumer.append("bar");
 assertEquals("foo + bar", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.append("foo");
 consumer.addNumber(1.2);
 assertEquals("fooi.two", consumer.getCode());
 }

 @Test
 public void testIsNegativeZero() {
 assertEquals(false, isNegativeZero(0.0));
 assertEquals(true, isNegativeZero(-0.0));
 }

 @Test
 public void testIsWordChar() {
 assertTrue(isWordChar('_'));
 assertTrue(isWordChar('$'));
 assertTrue(isWordChar('a'));
 assertTrue(isWordChar('1'));
 assertFalse(isWordChar(' '));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 fail("Not yet implemented");
 }

 @Test
 public void testEndFile() {
 fail("Not yet implemented");
 }

}

class TestCodeConsumer extends CodeConsumer {
 private StringBuilder code = new StringBuilder();
 @Override public void add(String s) { code.append(s); }
 @Override public void append(String s) { code.append(s); }
 @Override public char getLastChar() { return code.charAt(code.length() - 1); }
 @Override public String getCode() { return code.toString(); }
}