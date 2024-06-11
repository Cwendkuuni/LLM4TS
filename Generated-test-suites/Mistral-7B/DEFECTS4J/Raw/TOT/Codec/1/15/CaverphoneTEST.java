import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private final StringEncoder caverphone = new Caverphone();

    @Test
    public void testEmptyString() {
        String input = "";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSimpleString() {
        String input = "hello";
        String expectedOutput = "h2h2l2l2o2o";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStringWithNumbers() {
        String input = "hello123";
        String expectedOutput = "h2h2l2l2o2o222";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStringWithSpecialChars() {
        String input = "hello@world!";
        String expectedOutput = "h2h2l2l2o2oAwwA";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        String input = null;
        String expectedOutput = "1111111111"; // or any other default value
        String actualOutput = caverphone.encode(input);
        Assert.fail("Expected EncoderException, but got: " + actualOutput);
    }

    @Test
    public void testEqualStrings() {
        String input1 = "hello";
        String input2 = "hello";
        String expectedOutput = "h2h2l2l2o2o";
        String actualOutput1 = caverphone.encode(input1);
        String actualOutput2 = caverphone.encode(input2);
        Assert.assertEquals(expectedOutput, actualOutput1);
        Assert.assertEquals(expectedOutput, actualOutput2);
    }

    @Test
    public void testDifferentStrings() {
        String input1 = "hello";
        String input2 = "world";
        String expectedOutput1 = "h2h2l2l2o2o";
        String expectedOutput2 = "w2w2r2r2l2l2d2d";
        String actualOutput1 = caverphone.encode(input1);
        String actualOutput2 = caverphone.encode(input2);
        Assert.assertNotEquals(expectedOutput1, actualOutput2);
    }

    @Test
    public void testCaverphoneEqual() {
        String input1 = "hello";
        String input2 = "hello";
        boolean actualOutput = caverphone.isCaverphoneEqual(input1, input2);
        Assert.assertTrue(actualOutput);
    }

    @Test
    public void testCaverphoneNotEqual() {
        String input1 = "hello";
        String input2 = "world";
        boolean actualOutput = caverphone.isCaverphoneEqual(input1, input2);
        Assert.assertFalse(actualOutput);
    }

    @Test
    public void testCaseSensitivity() {
        String input1 = "HELLO";
        String input2 = "hello";
        String expectedOutput1 = "H2H2L2L2O2O";
        String expectedOutput2 = "h2h2l2l2o2o";
        String actualOutput1 = caverphone.encode(input1);
        String actualOutput2 = caverphone.encode(input2);
        Assert.assertNotEquals(expectedOutput1, actualOutput2);
        Assert.assertTrue(caverphone.isCaverphoneEqual(input1, actualOutput1));
        Assert.assertTrue(caverphone.isCaverphoneEqual(input2, actualOutput2));
    }

    @Test
    public void testTrimming() {
        String input = "   hello   ";
        String expectedOutput = "h2h2l2l2o2o";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncodingWrongType() {
        Object input = new Object();
        String expectedErrorMessage = "Parameter supplied to Caverphone encode is not of type java.lang.String";
        boolean actualExceptionThrown = false;
        try {
            caverphone.encode(input);
        } catch (EncoderException e) {
            String actualErrorMessage = e.getMessage();
            if (StringUtils.isEqual(expectedErrorMessage, actualErrorMessage)) {
                actualExceptionThrown = true;
            }
        }
        Assert.assertTrue(actualExceptionThrown);
    }
}