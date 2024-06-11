import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    // Test case for translate method
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Typical use case test
        StringBuilder input1 = new StringBuilder();
        input1.append("This is a test &#65; string.");
        StringBuilder expectedOutput1 = new StringBuilder();
        expectedOutput1.append("This is a test A string.");
        assertEquals(expectedOutput1.toString(), unescaper.translate(input1, 12, new StringBuilder()));

        // Edge case tests for hexadecimal entities
        StringBuilder input2 = new StringBuilder();
        input2.append("This is a test &#x41; string.");
        StringBuilder expectedOutput2 = new StringBuilder();
        expectedOutput2.append("This is a test A string.");
        assertEquals(expectedOutput2.toString(), unescaper.translate(input2, 12, new StringBuilder()));

        StringBuilder input3 = new StringBuilder();
        input3.append("This is a test &#x41B; string.");
        StringBuilder expectedOutput3 = new StringBuilder();
        expectedOutput3.append("This is a test 1071 string.");
        assertEquals(expectedOutput3.toString(), unescaper.translate(input3, 12, new StringBuilder()));

        // Edge case tests for decimal entities
        StringBuilder input4 = new StringBuilder();
        input4.append("This is a test &#65; string.");
        StringBuilder expectedOutput4 = new StringBuilder();
        expectedOutput4.append("This is a test A string.");
        assertEquals(expectedOutput4.toString(), unescaper.translate(input4, 12, new StringBuilder()));

        StringBuilder input5 = new StringBuilder();
        input5.append("This is a test &#100000000000; string."); // Integer overflow case
        StringBuilder expectedOutput5 = new StringBuilder();
        expectedOutput5.append("This is a test ; string."); // Invalid entity case
        assertEquals(expectedOutput5.toString(), unescaper.translate(input5, 12, new StringBuilder()));

        // Error scenario test
        StringBuilder input6 = new StringBuilder();
        input6.append("This is not a valid entity.");
        StringBuilder expectedOutput6 = new StringBuilder();
        expectedOutput6.append("This is not a valid entity.");
        assertEquals(expectedOutput6.toString(), unescaper.translate(input6, 15, new StringBuilder()));
    }
}