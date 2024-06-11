import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        assertEquals("Expected node passed to startSourceMapping", node, null);
      }

      @Override
      void endSourceMapping(Node node) {
        assertEquals("Expected node passed to endSourceMapping", node, null);
      }
    };

    consumer.startSourceMapping(null);
    consumer.endSourceMapping(null);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertFalse("Expected continueProcessing to return false", consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };

    assertEquals("Expected last char to be 'a'", 'a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("Expected string passed to append", str, "");
      }
    };

    consumer.append("Hello World");
    assertEquals("Expected appended string to be 'Hello World'", "Hello World", consumer.getLastChar() + "");
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer();

    consumer.append("print(\"Hello\");");
    consumer.endStatement();
    consumer.endStatement(true);

    assertEquals("Expected last char to be ';'", ';', consumer.getLastChar());
    assertEquals("Expected statementNeedsEnded to be true after endStatement(true)", true, consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFunction(boolean statementContext) {
        assertEquals("Expected statementContext to be false", false, statementContext);
      }
    };

    consumer.beginBlock();
    consumer.endFunction(true);
    consumer.endFunction();
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addIdentifier(String identifier) {
        assertEquals("Expected identifier passed to addIdentifier", identifier, "");
      }
    };

    consumer.addIdentifier("myVariable");
    assertEquals("Expected appended identifier to be 'myVariable'", "myVariable", consumer.getLastChar() + "");
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addNumber(double x) {
        assertEquals("Expected double passed to addNumber", 1.0, x);
      }
    };

    consumer.addNumber(1.0);
    assertEquals("Expected appended number to be '1.0'", "1.0", consumer.getLastChar() + "");
  }
}