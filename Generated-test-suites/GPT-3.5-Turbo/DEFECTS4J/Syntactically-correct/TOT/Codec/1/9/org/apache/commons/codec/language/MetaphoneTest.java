import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Typical use case: Testing a normal word
        String result1 = metaphone.metaphone("hello");
        Assert.assertEquals("HL", result1);

        // Typical use case: Testing a word with multiple vowels
        String result2 = metaphone.metaphone("house");
        Assert.assertEquals("HS", result2);

	// Edge case: Testing an empty string
        String result3 = metaphone.metaphone("");
        Assert.assertEquals("", result3);

        // Error scenario: Testing a word with special characters
        String result4 = metaphone.metaphone("h@ll!");
        Assert.assertEquals("HL", result4);

        // Error scenario: Testing a null string
        String result5 = metaphone.metaphone(null);
        Assert.assertEquals("", result5);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();

        // Typical use case: Testing encoding a normal word
        String result1 = metaphone.encode("hello");
        Assert.assertEquals("HL", result1);

        // Typical use case: Testing encoding a word with multiple vowels
        String result2 = metaphone.encode("house");
        Assert.assertEquals("HS", result2);

	// Edge case: Testing encoding an empty string
        String result3 = metaphone.encode("");
         Assert.assertEquals("", result3);

        // Error scenario: Testing encoding a word with special characters
        try {
            Object result4 = metaphone.encode("h@ll!");
        } catch (EncoderException e) {
            // Expected exception
        }

        // Error scenario: Testing encoding a null string
        try {
            Object result5 = metaphone.encode(null);
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        // Typical use case: Testing equality of two identical words
        boolean result1 = metaphone.isMetaphoneEqual("hello", "hello");
        Assert.assertTrue(result1);

        // Typical use case: Testing equality of two different words
        boolean result2 = metaphone.isMetaphoneEqual("hello", "house");
        Assert.assertFalse(result2);

        // Edge case: Testing equality of two empty strings
        boolean result3 = metaphone.isMetaphoneEqual("", "");
        Assert.assertTrue(result3);

        // Error scenario: Testing equality of a word with a null string
        boolean result4 = metaphone.isMetaphoneEqual("hello", null);
        Assert.assertFalse(result4);
    }
}