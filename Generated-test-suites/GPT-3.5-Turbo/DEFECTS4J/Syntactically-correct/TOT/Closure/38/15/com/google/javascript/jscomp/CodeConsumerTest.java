import org.junit.Test;

public class CodeConsumerTest {

    @Test
    public void testStartSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumer() {
            @Override
            void startSourceMapping(Node node) {
                // Test implementation
            }

            @Override
            char getLastChar() {
                // Test implementation
                return ' ';
            }

            @Override
            void append(String str) {
                // Test implementation
            }
        };

        // Test case for typical use case
        // ...

        // Test case for edge case
        // ...

        // Test case for error scenario
        // ...
    }

    @Test
    public void testEndSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumer() {
            @Override
            void endSourceMapping(Node node) {
                // Test implementation
            }

            @Override
            char getLastChar() {
                // Test implementation
                return ' ';
            }

            @Override
            void append(String str) {
                // Test implementation
            }
        };

        // Test case for typical use case
        // ...

        // Test case for edge case
        // ...

        // Test case for error scenario
        // ...
    }

    // Similar tests for other methods in CodeConsumer class

}