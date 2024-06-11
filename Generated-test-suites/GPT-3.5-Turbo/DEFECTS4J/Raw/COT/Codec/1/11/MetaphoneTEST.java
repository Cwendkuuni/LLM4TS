import org.junit.Test;
import org.junit.Assert;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test a single character word
        String result1 = metaphone.metaphone("A");
        Assert.assertEquals("A", result1);

        // Test a word with only consonants
        String result2 = metaphone.metaphone("BCD");
        Assert.assertEquals("BKT", result2);

        // Test a word with only vowels
        String result3 = metaphone.metaphone("AEIOU");
        Assert.assertEquals("A", result3);

        // Test a word with mixed consonants and vowels
        String result4 = metaphone.metaphone("HELLO");
        Assert.assertEquals("HL", result4);

        // Test a word with special characters
        String result5 = metaphone.metaphone("!@#$%^&*()");
        Assert.assertEquals("", result5);

        // Test an empty string
        String result6 = metaphone.metaphone("");
        Assert.assertEquals("", result6);
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        // Test encoding a string
        Object result1 = metaphone.encode("HELLO");
        Assert.assertEquals("HL", result1);

        // Test encoding an empty string
        Object result2 = metaphone.encode("");
        Assert.assertEquals("", result2);

        // Test encoding a non-string object
        boolean errorThrown = false;
        try {
            metaphone.encode(123);
        } catch (EncoderException e) {
            errorThrown = true;
        }
        Assert.assertTrue(errorThrown);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        // Test two strings with the same metaphone
        boolean result1 = metaphone.isMetaphoneEqual("HELLO", "HLL");
        Assert.assertTrue(result1);

        // Test two strings with different metaphones
        boolean result2 = metaphone.isMetaphoneEqual("HELLO", "WORLD");
        Assert.assertFalse(result2);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        int result = metaphone.getMaxCodeLen();
        Assert.assertEquals(5, result);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        int result = metaphone.getMaxCodeLen();
        Assert.assertEquals(5, result);
    }

}