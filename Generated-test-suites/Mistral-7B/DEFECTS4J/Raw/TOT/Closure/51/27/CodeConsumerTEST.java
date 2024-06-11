import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node(); // Dummy node for testing purpose
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Check that no exception is thrown and code is not modified
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAppendSimpleString() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("Hello World");
    String expectedCode = "Hello World";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatementAndBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (condition) {");
    consumer.endStatement();
    consumer.beginBlock();

    // Add some statements inside the block
    consumer.append("int x = 5;");
    consumer.append("int y = 10;");
    consumer.append("x += y;");

    // Check that the code is as expected
    String expectedCode = "if (condition) {\n" +
                          "  int x = 5;\n" +
                          "  int y = 10;\n" +
                          "  x += y;\n" +
                          "}";
    assertEquals(expectedCode, consumer.getCode());

    // Check that the block is ended properly
    consumer.endBlock();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("// some code here");
    consumer.endFunction();

    // Check that the code is as expected
    String expectedCode = "function foo() {\n" +
                          "  // some code here\n" +
                          "}";
    assertEquals(expectedCode, consumer.getCode());

    // Check that the block is ended properly
    consumer.endBlock();
    assertEquals("", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testContinueProcessingReturnsFalse() {
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    consumer.append("Hello World");
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  // Add more test cases here as needed

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void append(String str) {
      code.append(str);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }
}