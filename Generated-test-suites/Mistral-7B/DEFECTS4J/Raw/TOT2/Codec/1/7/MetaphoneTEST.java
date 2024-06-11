import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneBasic() {
        Metaphone encoder = new Metaphone();
        String input = "Apple";
        String expectedOutput = "AA0PLE";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone encoder = new Metaphone();
        String input = "";
        String expectedOutput = "";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone encoder = new Metaphone();
        String input = "A";
        String expectedOutput = "A";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullInput() {
        Metaphone encoder = new Metaphone();
        String input = null;
        encoder.metaphone(input);
    }

    @Test
    public void testMetaphoneWh() {
        Metaphone encoder = new Metaphone();
        String input = "What";
        String expectedOutput = "W00T";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneKn() {
        Metaphone encoder = new Metaphone();
        String input = "Knights";
        String expectedOutput = "K1TS";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneCia() {
        Metaphone encoder = new Metaphone();
        String input = "Cia";
        String expectedOutput = "X";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneSci() {
        Metaphone encoder = new Metaphone();
        String input = "Science";
        String expectedOutput = "S3NS";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneScythe() {
        Metaphone encoder = new Metaphone();
        String input = "Scythe";
        String expectedOutput = "S3NTh";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneSchool() {
        Metaphone encoder = new Metaphone();
        String input = "School";
        String expectedOutput = "SK00L";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneChild() {
        Metaphone encoder = new Metaphone();
        String input = "Child";
        String expectedOutput = "CH0LD";
        String actualOutput = encoder.metaphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneIsMetaphoneEqual() {
        Metaphone encoder = new Metaphone();
        String str1 = "Apple";
        String str2 = "Apple";
        boolean expectedOutput = true;
        boolean actualOutput = encoder.isMetaphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneIsMetaphoneEqualDifferentStrings() {
        Metaphone encoder = new Metaphone();
        String str1 = "Apple";
        String str2 = "Banana";
        boolean expectedOutput = false;
        boolean actualOutput = encoder.isMetaphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncode() {
        Metaphone encoder = new Metaphone();
        String input = "Apple";
        String expectedOutput = "AA0PLE";
        String actualOutput = encoder.encode(input).toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone encoder = new Metaphone();
        int maxCodeLen = encoder.getMaxCodeLen();
        int expectedMaxCodeLen = 4;
        Assert.assertEquals(expectedMaxCodeLen, maxCodeLen);
    }

    @Test
    public void testMetaphoneSetMaxCodeLen() {
        Metaphone encoder = new Metaphone();
        int newMaxCodeLen = 3;
        encoder.setMaxCodeLen(newMaxCodeLen);
        int actualMaxCodeLen = encoder.getMaxCodeLen();
        int expectedMaxCodeLen = 3;
        Assert.assertEquals(expectedMaxCodeLen, actualMaxCodeLen);
    }

    // Add test cases for other methods and edge cases
}