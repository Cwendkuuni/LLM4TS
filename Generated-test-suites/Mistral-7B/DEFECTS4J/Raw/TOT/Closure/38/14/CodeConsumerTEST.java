import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new MyCodeConsumer();
 }

 @Test
 public void testStartSourceMapping() {
 Node node = Node.newIntLiteral(1);
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 // Verify no errors were thrown
 }

 @Test
 public void testContinueProcessing() {
 consumer.continueProcessing = () -> false;
 Node node = Node.newIntLiteral(1);
 // Verify the code consumer stops processing
 consumer.startSourceMapping(node);
 consumer.append("Invalid code");
 assertFalse(consumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 Node node = Node.newStringLiteral("test");
 consumer.append(node);
 assertEquals('t', consumer.getLastChar());
 }

 @Test
 public void testAppend() {
 consumer.append("Hello");
 assertEquals("Hello", consumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 assertEquals("{\n", consumer.getCode());
 }

 @Test
 public void testEndBlock() {
 consumer.beginBlock();
 consumer.append("test");
 consumer.endBlock();
 assertEquals("{\ntest\n}", consumer.getCode());
 }

 @Test
 public void testAddIdentifier() {
 consumer.addIdentifier("test");
 assertEquals("test", consumer.getCode());
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(1.23);
 assertEquals("1.23", consumer.getCode());
 }

 @Test
 public void testAddOp() {
 consumer.addOp("+", false);
 assertEquals(" +", consumer.getCode());
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 assertEquals("", consumer.getCode());
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 assertEquals("\n", consumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.maybeEndStatement();
 assertEquals(";\n", consumer.getCode());
 }

 @Test
 public void testEndFunction() {
 consumer.beginBlock();
 consumer.addIdentifier("function");
 consumer.addIdentifier("TestFunction");
 consumer.append("(");
 consumer.addIdentifier("x");
 consumer.append(")");
 consumer.append("{");
 consumer.append("return x;");
 consumer.append("}");
 consumer.endFunction();
 assertEquals("function TestFunction(x){\nreturn x;\n}", consumer.getCode());
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 consumer.shouldPreserveExtraBlocks = () -> true;
 Node node = Node.newLiteral(Node.NEW_ARRAY, Node.newIntLiteral(1), Node.newIntLiteral(2));
 consumer.append("if (true) {");
 consumer.append("}");
 consumer.startSourceMapping(node);
 consumer.add(Node.newCode(Node.SCOPE, Node.newList(node), consumer.getCode()));
 consumer.endSourceMapping(node);
 assertEquals("if (true) {\n}", consumer.getCode());
 }

 @Test
 public void testBreakAfterBlockFor() {
 consumer.breakAfterBlockFor = (n, statementContext) -> true;
 Node node = Node.newLiteral(Node.NEW_ARRAY, Node.newIntLiteral(1), Node.newIntLiteral(2));
 consumer.append("for (int i = 0; i < 1; i++) {");
 consumer.append("}");
 consumer.startSourceMapping(node);
 consumer.add(Node.newCode(Node.SCOPE, Node.newList(node), consumer.getCode()));
 consumer.endSourceMapping(node);
 assertTrue(consumer.getCode().endsWith("\n}"));
 }

 @After
 public void tearDown() {
 consumer = null;
 }
}

class MyCodeConsumer extends CodeConsumer {
 @Override char getLastChar() {
 return super.getLastChar();
 }

 @Override String getCode() {
 return code;
 }
}