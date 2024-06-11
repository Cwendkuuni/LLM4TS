import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // Implement the test logic for startSourceMapping method
      }
    };

    // Test typical use case
    // ...

    // Test edge case
    // ...

    // Test error scenario
    // ...
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        // Implement the test logic for endSourceMapping method
      }
    };

    // Test typical use case
    // ...

    // Test edge case
    // ...

    // Test error scenario
    // ...
  }

  // Repeat the same pattern for each method in the CodeConsumer class
  // ...

}