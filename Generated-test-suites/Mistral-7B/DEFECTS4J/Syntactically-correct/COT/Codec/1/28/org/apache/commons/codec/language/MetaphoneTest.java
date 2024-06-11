import org.apache.commons.codec.CharUtils;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphoneEncoder;

    @Before
    public void setUp() {
        metaphoneEncoder = new Metaphone();
    }

    @Test
    public void testEmptyString() {
        String expected = "";
        String actual = metaphoneEncoder.metaphone("");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSingleCharacter() {
        String expected;
        String actual;

        for (char c = 'A'; c <= 'Z'; c++) {
            expected = String.valueOf(c).toUpperCase();
            actual = metaphoneEncoder.metaphone(String.valueOf(c));
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testBasicMetaphone() {
        String input = "banana";
        String expected = "B3211";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSilentG() {
        String input = "adagio";
        String expected = "A2301";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitialX() {
        String input = "exact";
        String expected = "X3211";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitialXWithFollowingVowel() {
        String input = "axiom";
        String expected = "A1221";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitialXWithTwoFollowingVowels() {
        String input = "axioma";
        String expected = "A1222";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitialXWithFollowingConsonant() {
        String input = "axiomatic";
        String expected = "A12213";
        String actual = metaphoneEncoder.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        String input = null;
        metaphoneEncoder.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyInputArray() {
        String[] inputs = {};
        for (String input : inputs) {
            metaphoneEncoder.metaphone(input);
        }
    }

    @Test(expected = EncoderException.class)
    public void testNonASCIIInput() {
        String input = "Mëtähpônê";
        metaphoneEncoder.metaphone(input);
    }

    @Test
    public void testEncodeDecode() {
        String input = "banana";
        String metaphoneCode = metaphoneEncoder.metaphone(input);
        String decodedString = metaphoneEncoder.decode(metaphoneCode);
        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeMultipleStrings() {
        String[] inputs = {"banana", "apple", "orange", "grape"};
        String[] metaphoneCodes = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            metaphoneCodes[i] = metaphoneEncoder.metaphone(inputs[i]);
        }

        String[] decodedStrings = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            decodedStrings[i] = metaphoneEncoder.decode(metaphoneCodes[i]);
        }

        for (int i = 0; i < inputs.length; i++) {
            Assert.assertEquals(inputs[i], decodedStrings[i]);
        }
    }

    @Test
    public void testMaxCodeLength() {
        Metaphone metaphoneEncoderWithMaxCodeLength5 = new Metaphone(5);
        String input = "bananarama";
        String expected = "B32113";
        String actual = metaphoneEncoderWithMaxCodeLength5.metaphone(input);
        Assert.assertEquals(expected, actual);
    }

}