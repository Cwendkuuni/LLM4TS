import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // No assertions needed, as method does not have any side effects
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // No assertions needed, as method does not have any side effects
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());

    consumer = new AlwaysFalseCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = consumer.getLastCode(); // assuming getLastCode is a method in CodeConsumer
    String lastChar = code.substring(code.length() - 1);
    // Some edge cases for testing: empty string, string with a single character, string with a long last character
    assertEquals(lastChar, "");
    assertEquals(lastChar, Character.toString(new Character((char) 0)));
    assertEquals(lastChar, Character.toString(new Character('\u00FF')));
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    String expectedCode = "expectedCode";
    consumer.append(expectedCode);
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    String actualCode = consumer.getCode();
    String expectedCode = "identifier";
    String lastChar = actualCode.substring(actualCode.length() - 1);
    assertTrue(lastChar.matches("[\\w$ _]")); // assuming last character is a valid identifier character
    assertEquals(expectedCode, actualCode.substring(0, actualCode.length() - 1));
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    String op = "+";
    consumer.addOp(op, false);
    String actualCode = consumer.getCode();
    String expectedCode = op;
   assertEquals(expectedCode, actualCode.substring(actualCode.length() - op.length()));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    double number = 123.45;
    consumer.addNumber(number);
    String actualCode = consumer.getCode();
    String expectedCode = Double.toString(number);
    assertEquals(expectedCode, actualCode.substring(actualCode.length() - expectedCode.length()));
  }

  private static class StubCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return 'a';
    }

    @Override
    String getCode() {
      return "code";
    }
  }

  private static class AlwaysFalseCodeConsumer extends CodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}