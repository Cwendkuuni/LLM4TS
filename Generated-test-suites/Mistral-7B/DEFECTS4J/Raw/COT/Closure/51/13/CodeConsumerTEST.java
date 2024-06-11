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
 consumer.startSourceMapping(null);
 }

 @Test
 public void testEndSourceMapping() {
 consumer.endSourceMapping(null);
 }

 @Test
 public void testContinueProcessing() {
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
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
 consumer.append("Hello World!");
 assertEquals("Hello World!", consumer.getCode());
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
 consumer.append("abc");
 assertEquals("\nabc", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 consumer.append("abc");
 consumer.maybeCutLine();
 consumer.append("def");
 assertEquals("abc\ndef", consumer.getCode());
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 consumer.append("abc");
 assertEquals("\nabc", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.append("abc");
 consumer.endBlock();
 assertEquals("abc}\n", consumer.getCode());
 }

 @Test
 public void testEndBlockWithLineBreak() {
 consumer.append("abc");
 consumer.endBlock(true);
 assertEquals("abc\n}", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 assertEquals(",\n", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.append("abc");
 consumer.endStatement();
 assertEquals("abc;\n", consumer.getCode());
 }

 @Test
 public void testEndStatementWithSemicolon() {
 consumer.append("abc");
 consumer.endStatement(true);
 assertEquals("abc;\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.append("abc");
 consumer.maybeEndStatement();
 consumer.append("def");
 assertEquals("abc;\ndef\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.append("function foo() {}\n");
 consumer.endFunction();
 consumer.append("abc");
 assertEquals("function foo() {}\n}\nabc\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.append("function foo() {}\n");
 consumer.endFunction(true);
 consumer.append("abc");
 assertEquals("function foo() {}\nabc\n", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 consumer.append("case 0:\n");
 consumer.endCaseBody();
 assertEquals("case 0:\n", consumer.getCode());
 }

 @Test
 public void testAdd() {
 consumer.append("abc");
 consumer.add("def");
 assertEquals("abcdef", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("1 + 1");
 consumer.addOp("+", true);
 assertEquals("1 + 1 +", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.append("1 + ");
 consumer.addNumber(2.5);
 assertEquals("1 + 2.5\n", consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testIsWordCharFailedCharacter() {
 assertFalse(isWordChar((char) 0));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 consumer = new TestCodeConsumerNoExtraBlocks();
 assertFalse(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 assertFalse(consumer.breakAfterBlockFor(node, false));
 assertTrue(consumer.breakAfterBlockFor(node, true));
 }

 @Test
 public void testEndFile() {
 consumer.append("abc");
 consumer.endFile();
 assertEquals("abc\n", consumer.getCode());
 }
}

class TestCodeConsumer extends CodeConsumer {
 String code = "";
 @Override
 void append(String str) {
 super.append(str);
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

class TestCodeConsumerNoExtraBlocks extends CodeConsumer {
 @Override
 boolean shouldPreserveExtraBlocks() {
 return false;
 }
}