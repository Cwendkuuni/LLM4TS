import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 Node node = Node.newIntLiteral(1);
 consumer.startSourceMapping(node);
 Node mappedNode = Node.newIntLiteral(2);
 consumer.endSourceMapping(mappedNode);
 assertTrue(consumer.sourceMapped(node).equals(mappedNode));
 }

 @Test
 public void testContinueProcessing() {
 assertFalse(consumer.continueProcessing());
 }

 @Test(expected = AssertionError.class)
 public void testGetLastChar_empty() {
 consumer.getLastChar();
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
 String code = "if (x < 10) {\n  y = 5;\n} if (x > 15) {\n  z = 10;\n} else {\n  w = 15;\n}";
 consumer.setCode(code);
 consumer.maybeLineBreak();
 assertEquals("if (x < 10) {\n  y = 5;\n}\nif (x > 15) {\n  z = 10;\n}", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 String code = "if (x < 10) {\n  y = 5;\n} if (x > 15) {\n  z = 10;\n} else {\n  w = 15;\n}";
 consumer.setCode(code);
 consumer.maybeCutLine();
 assertEquals("if (x < 10) { y = 5;\n} if (x > 15) { z = 10;\n}", consumer.getCode());
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
 consumer.beginBlock();
 consumer.append("x = 5;");
 consumer.endBlock();
 assertEquals("{\nx = 5;\n}", consumer.getCode());
 }

 @Test
 public void testEndBlockWithNewline() {
 consumer.beginBlock();
 consumer.append("x = 5;");
 consumer.endBlock(true);
 assertEquals("{\nx = 5;\n}", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.append("x = 5;");
 consumer.listSeparator();
 consumer.append("y = 10;");
 assertEquals("x = 5;\n y = 10;\n", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.append("x = 5;");
 consumer.endStatement();
 assertEquals("x = 5;\n", consumer.getCode());
 }

 @Test
 public void testEndStatementWithNewline() {
 consumer.append("x = 5;");
 consumer.endStatement(true);
 assertEquals("x = 5;\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.append("x = 5;");
 consumer.maybeEndStatement();
 consumer.append("y = 10;");
 assertEquals("x = 5;\n y = 10;\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.append("function foo() { x = 5; }");
 consumer.endFunction();
 assertEquals("function foo() {\nx = 5;\n}", consumer.getCode());
 }

 @Test
 public void testEndFunctionWithNewline() {
 consumer.append("function foo() { x = 5; }");
 consumer.endFunction(true);
 assertEquals("function foo() {\nx = 5;\n}", consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 assertEquals(":\n", consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 consumer.beginCaseBody();
 consumer.append("x = 5;");
 consumer.endCaseBody();
 assertEquals(":\nx = 5;\n", consumer.getCode());
 }

 @Test
 public void testAdd() {
 consumer.append("x = 5;");
 consumer.add("y = 10;");
 assertEquals("x = 5;\ny = 10;\n", consumer.getCode());
 }

 @Test
 public void testAddIdentifier() {
 consumer.append("x = 5;");
 consumer.addIdentifier("y");
 assertEquals("x = 5;y;\n", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("x = 5;");
 consumer.addOp("+", true);
 consumer.append("y = 10;");
 consumer.addOp("+", true);
 consumer.append("z = x + y;");
 assertEquals("x = 5;\n+ \n+ y = 10;\nz = x + y;\n", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.append("x = 5;");
 consumer.addNumber(3.14);
 assertEquals("x = 5;\n3.14;\n", consumer.getCode());
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor_statement() {
 consumer.setCode("if (x < 10) { y = 5; }");
 assertTrue(consumer.breakAfterBlockFor(Node.newBooleanLiteral(true), false));
 assertEquals("if (x < 10) { y = 5;\n}", consumer.getCode());
 }

 @Test
 public void testBreakAfterBlockFor_expression() {
 consumer.setCode("if (x < 10) { return 5; }");
 assertFalse(consumer.breakAfterBlockFor(Node.newIntLiteral(1), false));
 assertEquals("if (x < 10) { return 5;\n}", consumer.getCode());
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
 }

}

class TestCodeConsumer extends CodeConsumer {
 @Override
 char getLastChar() {
 return consumer.getCode().charAt(consumer.getCode().length() - 1);
 }

 @Override
 String getCode() {
 return consumer.code;
 }

 @Override
 void setCode(String code) {
 super.code = code;
 }
}