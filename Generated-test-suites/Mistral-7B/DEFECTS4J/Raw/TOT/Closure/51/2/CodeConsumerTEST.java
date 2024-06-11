import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append(" hello");
    consumer.endSourceMapping(node);

    String expectedSourceMap = "2:1-2:5";
    String actualSourceMap = consumer.getSourceMapping(node);

    assertTrue(actualSourceMap.equals(expectedSourceMap));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.setContinueProcessing(false);
    String input = "function test() {}";
    consumer.process(input);
    String output = consumer.getCode();

    assertFalse(output.contains("function test()"));
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    String expected = "hello";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAppendBlockStartAndAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("if (condition) {\n");
    consumer.appendBlockEnd();
    String expected = "if (condition) {\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {\n");
    consumer.startNewLine();
    consumer.append("  var x = 5;\n");
    String expected = "function test() {\n  var x = 5;\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {\n");
    consumer.maybeLineBreak();
    consumer.append("  var x = 5;\n");
    String expected = "function test() {\n\n  var x = 5;\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBeginBlockAndEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("if (condition) {\n");
    consumer.append("  var x = 5;\n");
    consumer.endBlock();
    String expected = "if (condition) {\n  var x = 5;\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("[1, 2, ");
    consumer.listSeparator();
    consumer.append("3, 4]");
    String expected = "[1, 2, 3, 4]";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("var x = 5;\n");
    consumer.endStatement();
    String expected = "var x = 5;\n";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {\n");
    consumer.append("  return 5;\n}");
    consumer.endFunction();
    String expected = "function test() {\n  return 5;\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBeginCaseBodyAndEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("return 5;\n");
    consumer.endCaseBody();
    String expected = "return 5;\n";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("var x = 5");
    String expected = "var x = 5";;
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(3.14159);
    String expected = "3.14159";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    String code = "for (var i = 0, len = arr.length; i < len; ++i) {\n  foo(arr[i]);\n}";
    consumer.setShouldPreserveExtraBlocks(true);
    consumer.process(code);
    String actual = consumer.getCode();

    String expected = "for (var i = 0, len = arr.length; i < len; ++i) {\n  foo(arr[i]);\n}";
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.append("function test() {\n  if (condition) {\n    foo();\n  }}\n");
    boolean result = consumer.breakAfterBlockFor(node, true);
    String expected = "function test() {\n  if (condition) {\n    foo();\n  }\n  // Newline here\n}";
    String actual = consumer.getCode();

    assertTrue(result);
    assertTrue(expected.substring(0, expected.lastIndexOf("\n")).equals(actual));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.endFile();
    String expected = "function test() {\n}";
    String actual = consumer.getCode();

    assertTrue(expected.equals(actual));
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getSourceMapping(Node node) {
    return "";
  }

  @Override
  void setContinueProcessing(boolean continueProcessing) {
    // Empty implementation
  }

  @Override
  void process(String code) {
    // Empty implementation
  }

  @Override
  String getCode() {
    return "";
  }
}