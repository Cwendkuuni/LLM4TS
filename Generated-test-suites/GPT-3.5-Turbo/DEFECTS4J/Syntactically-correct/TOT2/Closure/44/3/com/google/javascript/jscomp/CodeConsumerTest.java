import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    // Test for startSourceMapping method
    CodeConsumer codeConsumer = new CodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // Add test logic here
      }

      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void append(String str) {

      }
    };

    // Add test cases here
  }

  @Test
  public void testEndSourceMapping() {
    // Test for endSourceMapping method
    CodeConsumer codeConsumer = new CodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        // Add test logic here
      }

      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void append(String str) {

      }
    };

    // Add test cases here
  }

  // Add test cases for all other public methods in CodeConsumer class

}