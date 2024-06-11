import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "Hello World";
        String expectedOutput = "HELLOWORLD";
        String output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);

        // Edge case: empty string
        input = "";
        expectedOutput = "";
        output = SoundexUtils.clean(input);
        assertEquals(expectedOutput, output);

        // Error scenario: null input
        input = null;
        expectedOutput = null;
        output = SoundexUtils.clean(input);
        assertNull(output);
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case
        StringEncoder encoder = new SomeEncoder();
        String s1 = "Hello";
        String s2 = "Hallo";
        int expectedOutput = 4;
        int output = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, output);

        // Edge case: empty strings
        s1 = "";
        s2 = "";
        expectedOutput = 0;
        output = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, output);

        // Error scenario: encoder throws EncoderException
        encoder = new MalfunctioningEncoder();
        s1 = "Hello";
        s2 = "Hallo";
        try {
            output = SoundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException not thrown");
        } catch (EncoderException e) {
            // exception expected
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "1234";
        String es2 = "1255";
        int expectedOutput = 2;
        int output = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, output);

        // Edge case: empty strings
        es1 = "";
        es2 = "";
        expectedOutput = 0;
        output = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, output);

        // Error scenario: null input
        es1 = null;
        es2 = "1234";
        expectedOutput = 0;
        output = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, output);
    }
    
    // Additional test cases can be added for each method
    
}