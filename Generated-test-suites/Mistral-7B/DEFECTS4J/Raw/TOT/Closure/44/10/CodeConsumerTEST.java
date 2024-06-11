import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

 @Test
 public void testStartSourceMappingAndEndSourceMapping() {
 CodeConsumer consumer = new MyCodeConsumer();
 Node node = new Node(); // create a dummy node for testing

 consumer.startSourceMapping(node);
 consumer.append("some code");
 consumer.endSourceMapping(node);

 String expectedSourceMap = "1:1-1:6"; // assuming the generated code is "some code" and the node's source is "someCode.js:1:1"
 String actualSourceMap = consumer.getSourceMapping();
 assertEquals(expectedSourceMap, actualSourceMap);
 }

 @Test
 public void testContinueProcessing() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.continueProcessing(); // should continue processing

 CodeConsumer consumer2 = new MyCodeConsumer() {
 @Override
 public boolean continueProcessing() {
 return false;
 }
 };
 assertFalse(consumer2.continueProcessing()); // should stop processing
 }

 @Test
 public void testGetLastChar() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("abc");
 char lastChar = consumer.getLastChar();
 assertEquals('c', lastChar);
 }

 @Test
 public void testAppend() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("Hello, World!");
 String expectedCode = "Hello, World!";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testEndStatement() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("int x = 5;");
 consumer.endStatement();
 String expectedCode = "int x = 5;\n";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testEndFunction() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("function foo() {}\n");
 consumer.endFunction();
 String expectedCode = "function foo() {\n}\n";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testPrettyPrinting() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("void foo() { return x++ + ++y; }");
 consumer.endFunction();
 String expectedCode = "void foo() {\n  return x++ + ++y;\n}\n";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.shouldPreserveExtraBlocks = true;
 String code = "if (x > 5) { x++; }";
 consumer.append(code);
 String expectedCode = "if (x > 5) {\n  x++;\n}";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testBreakAfterBlockFor() {
 CodeConsumer consumer = new MyCodeConsumer();
 Node node = new Node(); // create a dummy node for testing
 consumer.breakAfterBlockFor = (n, statementContext) -> true;
 consumer.append("if (true) { x = 5; }");
 String expectedCode = "if (true) {\n  x = 5;\n}";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testEndFile() {
 CodeConsumer consumer = new MyCodeConsumer();
 consumer.append("// This is a comment\n");
 consumer.endFile();
 String expectedCode = "// This is a comment\n";
 String actualCode = consumer.getCode();
 assertEquals(expectedCode, actualCode);
 }
}