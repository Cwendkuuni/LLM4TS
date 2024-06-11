import org.junit.Test;

import static org.junit.Assert.*;

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
                return 0;
            }

            @Override
            void append(String str) {
                // Test implementation
            }
        };

        // Test case for typical use case
        // Assert that the startSourceMapping method is implemented correctly
        // ...

        // Test case for edge case
        // Assert that the startSourceMapping method works for a null input
        // ...

        // Test case for error scenario
        // Assert that the startSourceMapping method throws an exception when ...
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
                return 0;
            }

            @Override
            void append(String str) {
                // Test implementation
            }
        };

        // Test case for typical use case
        // Assert that the endSourceMapping method is implemented correctly
        // ...

        // Test case for edge case
        // Assert that the endSourceMapping method works for a null input
        // ...

        // Test case for error scenario
        // Assert that the endSourceMapping method throws an exception when ...
        // ...
    }

    // Repeat the above template for each method in the CodeConsumer class

}