import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        assertEquals(node, this.lastNode);
        this.lastNode = node;
      }

      Node lastNode = null;
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNotNull(consumer.lastNode);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        assertEquals(node, this.lastNode);
        this.lastNode = null;
      }

      Node lastNode = null;
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.lastNode);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return this.lastChar;
      }

      char lastChar = 'A';
    };

    consumer.add("ABC");
    assertEquals('C', consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endStatement() {
        this.lastStatementEnded = true;
      }

      boolean lastStatementEnded = false;
    };

    consumer.startBlock();
    consumer.add("int a = 5;");
    consumer.endBlock();
    assertTrue(consumer.lastStatementEnded);
  }

  @Test
  public void testAddIdentifierWithEmptyString() {
    CodeConsumer consumer = new CodeConsumer();
    consumer.beginBlock();
    consumer.addIdentifier("");
    consumer.endBlock();

    // This test should fail as the given code consumes an empty string,
    // but it should throw an AssertionError as the test case expects it.
    fail("Expected an AssertionError");
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer();
    consumer.beginBlock();
    consumer.add("int a = 5;");
    consumer.endBlock();

    String expected = "{int a = 5;}";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddWithInvalidString() {
    CodeConsumer consumer = new CodeConsumer();
    consumer.beginBlock();
    consumer.add("int a = 5");
    consumer.endBlock();

    // This test should fail as the given code consumes an incomplete string
    String expected = "{int a = 5}";
    String actual = consumer.toString();
    assertNotEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer();
    consumer.append("hello");
    consumer.append(" world!");

    String expected = "helloworld!";
    String actual = consumer.toString();
    assertEquals(expected, actual);
  }

  // Test other methods with similar logic
}