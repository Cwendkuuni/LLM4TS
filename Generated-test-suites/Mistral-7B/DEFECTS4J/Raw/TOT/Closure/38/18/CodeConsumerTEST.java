import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new MyCodeConsumer(); // Replace with your concrete implementation
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Check if source mapping has been started and finished correctly
    // You can use reflection or any other mechanism to check the state of the consumer
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());

    consumer = spy(new MyCodeConsumer());
    doReturn(false).when(consumer).continueProcessing();

    // Test the behavior when continueProcessing returns false
    // You can use any testing framework to test the flow of the code
  }

  @Test
  public void testGetLastChar() {
    consumer.append("hello");
    consumer.append("world");

    assertEquals('w', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    consumer.append("world");

    String expected = "helloworld";
    assertEquals(expected, consumer.getCode()); // Assuming getCode() method returns the generated code
  }

  @Test
  public void testEndLine() {
    consumer.append("hello");
    consumer.endLine();
    consumer.append("world");

    String expected = "helloworld\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("int x = 5;");
    consumer.endStatement();

    String expected = "int x = 5;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.addIdentifier("functionName");
    consumer.append("() {");
    consumer.append("return 5;");
    consumer.endFunction();

    String expected = "function functionName() {\n  return 5;\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(3.14);

    String expected = "3.14\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");

    String expected = "identifier\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFile() {
    consumer.append("int main() {}\n");
    consumer.endFile();

    String expected = "int main() {\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  // Add more test cases covering different methods and scenarios
}