import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = createNode();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    String expected = "hello";
    StringBuilder actual = new StringBuilder();
    consumer.append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    consumer.append("world");
    String result = consumer.toString();
    assertTrue(result.contains("\nworld"));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.add("if (x > 0) {");
    consumer.add("x++;");
    consumer.endBlock();
    String result = consumer.toString();
    assertTrue(result.contains("if (x > 0) {\n  x++;\n}"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    String result = consumer.toString();
    assertTrue(result.contains("function foo() {\n}\n"));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(123.45);
    consumer.addNumber(45.67);
    String result = consumer.toString();
    assertTrue(result.contains("123.45\n45.67"));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = createNode();
    assertTrue(consumer.shouldPreserveExtraBlocks());
    consumer.add("for (int i = 0; i < 10; i++) { i++; }");
    String result = consumer.toString();
    assertTrue(result.contains("for (int i = 0; i < 10; i++) {\n  i++;\n}"));
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = createNode();
    assertTrue(consumer.breakAfterBlockFor(node, true));
    consumer.append("for (int i = 0; i < 10; i++) { i++; }");
    String result = consumer.toString();
    assertTrue(result.contains("\ni++;\n"));
  }

  private Node createNode() {
    // Create and return a Node instance for testing.
    return new Node();
  }
}