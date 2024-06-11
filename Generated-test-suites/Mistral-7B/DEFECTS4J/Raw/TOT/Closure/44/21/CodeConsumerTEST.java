import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CodeConsumerTest {

 private CodeConsumer codeConsumer;
 private Node mockNode;

 @Before
 public void setUp() {
 mockNode = mock(Node.class);
 codeConsumer = new TestCodeConsumer(); // Use your concrete implementation of CodeConsumer
 }

 @Test
 public void testStartSourceMappingAndEndSourceMapping() {
 codeConsumer.startSourceMapping(mockNode);
 codeConsumer.endSourceMapping(mockNode);
 verify(mockNode, times(2)).setLineAndColumn(anyInt(), anyInt());
 }

 @Test
 public void testContinueProcessing() {
 assertTrue(codeConsumer.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 codeConsumer.append("abc");
 assertEquals('c', codeConsumer.getLastChar());
 }

 @Test
 public void testAppend() {
 codeConsumer.append("Hello World!");
 String expected = "Hello World!\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testAddIdentifier() {
 codeConsumer.addIdentifier("identifier");
 String expected = "identifier\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testAddNumber() {
 codeConsumer.addNumber(123.45);
 String expected = "123.45\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testAddOp() {
 codeConsumer.addOp("+", false);
 codeConsumer.addNumber(1);
 codeConsumer.addOp("+", false);
 codeConsumer.addNumber(2);
 String expected = "1 + 2\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testBeginBlock() {
 codeConsumer.beginBlock();
 String expected = "{\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testEndBlock() {
 codeConsumer.append("print('Hello World!');");
 codeConsumer.endBlock(true);
 String expected = "}\nprint('Hello World!');\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testListSeparator() {
 codeConsumer.append("foo");
 codeConsumer.listSeparator();
 codeConsumer.append("bar");
 String expected = "foo,\nbar\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testMaybeEndStatement() {
 codeConsumer.append("int x = 1;");
 codeConsumer.maybeEndStatement();
 String expected = "int x = 1;\n";
 assertEquals(expected, codeConsumer.getCode());
 }

 @Test
 public void testSawFunction() {
 codeConsumer.beginBlock();
 codeConsumer.append("function foo() {}", CodeConsumer.FunctionType.FUNCTION_DECLARATION);
 codeConsumer.endFunction(true);
 codeConsumer.append("function bar() {}", CodeConsumer.FunctionType.FUNCTION_DECLARATION);
 codeConsumer.endFunction(true);
 assertTrue(codeConsumer.sawFunction);
 }

 @Test
 public void testEndFile() {
 codeConsumer.endFile();
 assertEquals("", codeConsumer.getCode());
 }
}