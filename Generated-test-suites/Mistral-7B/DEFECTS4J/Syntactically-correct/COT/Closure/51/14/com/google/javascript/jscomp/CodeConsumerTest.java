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
 Boolean result = consumer.continueProcessing();
 assertTrue(result);
 }

 @Test
 public void testGetLastChar() {
 char lastChar = consumer.getLastChar();
 assertEquals(0, lastChar);
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("identifier");
 String expectedCode = "identifier";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testAppend() {
 consumer.append("testString");
 String expectedCode = "testString";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 String expectedCode = "{\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 String expectedCode = "}\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 String expectedCode = "\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testMaybeLineBreak() {
 consumer.maybeLineBreak();
 String expectedCode = "\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 String expectedCode = "\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 String expectedCode = "{\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.endBlock();
 String expectedCode = "}\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 String expectedCode = ", \n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndStatement() {
 consumer.endStatement();
 String expectedCode = "\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndStatementWithSemiColon() {
 consumer.endStatement(true);
 String expectedCode = "; \n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.maybeEndStatement();
 String expectedCode = "\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.endFunction();
 String expectedCode = "}\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndFunctionWithStatementContext() {
 consumer.endFunction(true);
 String expectedCode = "\n}\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 String expectedCode = ": \n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testEndCaseBody() {
 consumer.endCaseBody();
 }

 @Test
 public void testAdd() {
 consumer.add("test");
 consumer.add("String");
 String expectedCode = "testString\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", true);
 consumer.add("1");
 consumer.addOp("+", true);
 consumer.add("2");
 String expectedCode = "1 + 2\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(1.2);
 String expectedCode = "1.2\n";
 assertEquals(expectedCode, consumer.getCode());
 }

 @Test(expected = AssertionError.class)
 public void testIsWordCharWithInvalidChar() {
 assertFalse(CodeConsumer.isWordChar('%'));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertFalse(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockForTrueStatementContext() {
 Node node = new Node_();
 boolean result = consumer.breakAfterBlockFor(node, true);
 assertTrue(result);
 }

 @Test
 public void testBreakAfterBlockForFalseStatementContext() {
 Node node = new Node_();
 boolean result = consumer.breakAfterBlockFor(node, false);
 assertFalse(result);
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThrowExceptionWhenGetCodeIsCalledBeforeSettingCode() {
 consumer.getCode();
 }
}

 class StubCodeConsumer extends CodeConsumer {
 String code = "";

 @Override
 abstract char getLastChar() {
 return 0;
 }

 @Override
 void add(String newcode) {
 code += newcode;
 }

 @Override
 void append(String str) {
 add(str);
 }

 @Override
 void appendBlockStart() {
 add("{");
 }

 @Override
 void appendBlockEnd() {
 add("}");
 }

 @Override
 void startNewLine() {
 add("\n");
 }

 @Override
 void maybeLineBreak() {
 add("\n");
 }

 @Override
 void maybeCutLine() {
 // empty
 }

 @Override
 void endLine() {
 add("\n");
 }

 @Override
 void notePreferredLineBreak() {
 // empty
 }

 @Override
 void beginBlock() {
 // empty
 }

 @Override
 void endBlock(boolean shouldEndLine) {
 // empty
 }

 @Override
 void listSeparator() {
 add(", \n");
 }

 @Override
 void endStatement() {
 // empty
 }

 @Override
 void endStatement(boolean needSemiColon) {
 // empty
 }

 @Override
 void maybeEndStatement() {
 // empty
 }

 @Override
 void endFunction() {
 // empty
 }

 @Override
 void endFunction(boolean statementContext) {
 // empty
 }

 @Override
 void beginCaseBody() {
 // empty
 }

 @Override
 void endCaseBody() {
 // empty
 }

 @Override
 void addIdentifier(String identifier) {
 add(identifier);
 }

 @Override
 void addOp(String op, boolean binOp) {
 add(op);
 }

 @Override
 void addNumber(double x) {
 addNumber(x);
 }

 @Override
 boolean shouldPreserveExtraBlocks() {
 return false;
 }

 @Override
 boolean breakAfterBlockFor(Node n, boolean statementContext) {
 return statementContext;
 }

 @Override
 void endFile() {
 // empty
 }

 @Override
 String getCode() {
 return code;
 }
 }