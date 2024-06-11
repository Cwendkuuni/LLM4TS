import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node("Identifier", "test");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    String expectedSourceMap = "{\"version\":1,\"file\":\"CodeConsumerTest.java\",\"sources\":[{\"file\":\"CodeConsumerTest.java\",\"lineNumber\":1}],\"sourcesContent\":[\"CodeConsumerTest.java\"],\"names\":[],\"mappings\":[\"\\u0000,\\u0000,test,0:1,test,1:1\"]}";
    String actualSourceMap = consumer.getSourceMap();
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    char lastChar = consumer.getLastChar();
    assertEquals('t', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String expected = "{";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("test");
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(42.2);
    String expected = "42.2";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("5");
    consumer.addOp("+", true);
    consumer.append("3");
    String expected = "5 + 3";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    String expected = "function test() {\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node("Identifier", "test"), false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }
}