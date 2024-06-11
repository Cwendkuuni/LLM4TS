
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Basic JUnit test case for the filter method
        // Test Case 1: input message is null
        Assert.assertNull(HTMLFilter.filter(null));

        // Test Case 2: input message does not contain any HTML special characters
        String input1 = "Hello, this is a test message.";
        String expectedOutput1 = "Hello, this is a test message.";
        Assert.assertEquals(expectedOutput1, HTMLFilter.filter(input1));

        // Test Case 3: input message contains HTML special characters
        String input2 = "This is a <strong>test</strong> message.";
        String expectedOutput2 = "This is a &lt;strong&gt;test&lt;/strong&gt; message.";
        Assert.assertEquals(expectedOutput2, HTMLFilter.filter(input2));
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    // Test Case 4: empty input message
    @Test
    public void testFilter_emptyInput() {
        String input = "";
        String expectedOutput = "";
        Assert.assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Test Case 5: input message with only HTML special characters
    @Test
    public void testFilter_specialCharactersOnly() {
        String input = "<>&\"";
        String expectedOutput = "&lt;&gt;&amp;&quot;";
        Assert.assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Test Case 6: input message with a mix of HTML special characters and regular text
    @Test
    public void testFilter_mixedText() {
        String input = "This is a <strong>test</strong> message with &lt;special&gt; characters.";
        String expectedOutput = "This is a &lt;strong&gt;test&lt;/strong&gt; message with &amp;lt;special&amp;gt; characters.";
        Assert.assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
}

