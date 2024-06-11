import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer = new TestCodeConsumer();

 @Test
 public void testStartSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 assertNull(consumer.getLastChar());
 consumer.append("test");
 consumer.endSourceMapping(node);
 assertEquals("test", node.toSource());
 }

 @Test
 public void testContinueProcessing() {
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 consumer.append("test");
 assertEquals('t', consumer.getLastChar());
 }

 @Test
 public void testAppend() {
 consumer.append("test");
 assertEquals("test", consumer.getCode());
 }

 @Test
 public void testAppendBlockStart() {
 consumer.beginBlock();
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
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 assertTrue(consumer.statementNeedsEnded);
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 assertTrue(consumer.statementNeedsEnded);
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 consumer.append("test");
 consumer.endBlock();
 assertEquals("{test}\n", consumer.getCode());
 }

 @Test
 public void testEndBlockFalse() {
 consumer.beginBlock();
 consumer.append("test");
 consumer.endBlock();
 consumer.append("test2");
 consumer.endBlock(false);
 assertEquals("{test}\n{test2}", consumer.getCode());
 }

 @Test
 public void testEndBlockTrue() {
 consumer.beginBlock();
 consumer.append("test");
 consumer.endBlock();
 consumer.append("test2");
 consumer.endBlock(true);
 assertEquals("{test}\n{test2}\n", consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 consumer.append("test");
 assertEquals("test,\n", consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.append("test");
 consumer.endStatement();
 consumer.append("test2");
 assertEquals("test;\ntest2", consumer.getCode());
 }

 @Test
 public void testEndStatementNeedSemiColon() {
 consumer.append("test");
 consumer.endStatement(true);
 consumer.append("test2");
 assertEquals("test;\ntest2", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.append("test");
 consumer.maybeEndStatement();
 consumer.append("test2");
 assertEquals("test\ntest2", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.beginBlock();
 consumer.append("function test() {");
 consumer.append("\ttest();");
 consumer.endFunction();
 assertEquals("function test() {\ntest();\n}\n", consumer.getCode());
 }

 @Test
 public void testEndFunctionStatementContext() {
 consumer.beginBlock();
 consumer.append("function test() {");
 consumer.append("\ttest();");
 consumer.endFunction(true);
 assertEquals("function test() {\ntest();}\n", consumer.getCode());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("test");
 assertEquals("test", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.append("1");
 consumer.addOp("+", false);
 consumer.append("2");
 assertEquals("1+\n2", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(1.23);
 assertEquals("1.23\n", consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidIsWordChar() {
 char invalid = '!';
 assertTrue(isWordChar(invalid));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 node.setLine(1);
 Node statement = new Node_Statement();
 node.appendChild(statement);
 assertFalse(consumer.breakAfterBlockFor(node, false));
 }

 @Test
 public void testBreakAfterBlockForStatementContext() {
 Node node = new Node();
 node.setLine(1);
 Node statement = new Node_Statement();
 node.appendChild(statement);
 assertTrue(consumer.breakAfterBlockFor(node, true));
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 assertEquals("", consumer.getCode());
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
 void appendBlockStart() {
 super.appendBlockStart();
 }

 @Override
 void appendBlockEnd() {
 super.appendBlockEnd();
 }

 @Override
 void startNewLine() {
 super.startNewLine();
 }

 @Override
 void maybeCutLine() {
 super.maybeCutLine();
 }

 @Override
 void endLine() {
 super.endLine();
 }

 @Override
 void notePreferredLineBreak() {
 super.notePreferredLineBreak();
 }

 @Override
 void beginBlock() {
 super.beginBlock();
 }

 @Override
 void endBlock() {
 super.endBlock();
 }

 @Override
 void listSeparator() {
 super.listSeparator();
 }

 @Override
 void endStatement() {
 super.endStatement();
 }

 @Override
 void endStatement(boolean needSemiColon) {
 super.endStatement(needSemiColon);
 }

 @Override
 void maybeEndStatement() {
 super.maybeEndStatement();
 }

 @Override
 void endFunction() {
 super.endFunction();
 }

 @Override
 void endFunction(boolean statementContext) {
 super.endFunction(statementContext);
 }

 @Override
 void beginCaseBody() {
 super.beginCaseBody();
 }

 @Override
 void endCaseBody() {
 super.endCaseBody();
 }

 @Override
 void add(String newcode) {
 super.add(newcode);
 }

 @Override
 void addNumber(double x) {
 super.addNumber(x);
 }

 @Override
 boolean isWordChar(char ch) {
 return super.isWordChar(ch);
 }

 @Override
 boolean shouldPreserveExtraBlocks() {
 return super.shouldPreserveExtraBlocks();
 }

 @Override
 boolean breakAfterBlockFor(Node n, boolean statementContext) {
 return super.breakAfterBlockFor(n, statementContext);
 }

 @Override
 void endFile() {
 super.endFile();
 }
}