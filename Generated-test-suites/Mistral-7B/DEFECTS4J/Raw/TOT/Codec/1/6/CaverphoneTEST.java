import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private final StringEncoder encoder = new Caverphone();

    @Test
    public void testEmptyString() {
        String expected = "1111111111";
        String actual = encoder.encode("");
        Assert.assertEquals("Caverphone of an empty string should be '1111111111'", expected, actual);
    }

    @Test
    public void testNullString() {
        String input = null;
        String expectedErrorMessage = "Parameter supplied to Caverphone encode is null";

        try {
            encoder.encode(input);
            Assert.fail("Expected EncoderException for null input");
        } catch (EncoderException e) {
            Assert.assertEquals("Expected error message for null input", expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    public void testValidString() {
        String input = "HelloWorld";
        String expected = "H2oW0r3d";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'HelloWorld' should be 'H2oW0r3d'", expected, actual);
    }

    @Test
    public void testValidStringWithStartOptions() {
        String input = "CoughCoughEnoughTrough";
        String expected = "Cou2fEnou2fTrou2f";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'CoughCoughEnoughTrough' should be 'Cou2fEnou2fTrou2f'", expected, actual);
    }

    @Test
    public void testValidStringWithReplacements() {
        String input = "QuietCinquefoilCecilyCecilyCecilieQuiet";
        String expected = "QuietSiSiSiQuiet";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'QuietCinquefoilCecilyCecilyCecilieQuiet' should be 'QuietSiSiSiQuiet'", expected, actual);
    }

    @Test
    public void testValidStringWithRemovals() {
        String input = "ThreesixteenTwentythreeThirtythreeTwentythreeThreesixteen";
        String expected = "ThreesixteenTwentythreeThreesixteen";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'ThreesixteenTwentythreeThirtythreeTwentythreeThreesixteen' should be 'ThreesixteenTwentythreeThreesixteen'", expected, actual);
    }

    @Test
    public void testValidStringWithHandles() {
        String input = "CoughCoughEnoughTroughCoughCoughEnoughTroughGnMb";
        String expected = "Cou2fEnou2fTrou2fCou2fEnou2fTrou2fM2";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'CoughCoughEnoughTroughCoughCoughEnoughTroughGnMb' should be 'Cou2fEnou2fTrou2fCou2fEnou2fTrou2fM2'", expected, actual);
    }

    @Test
    public void testValidStringWithHandlesAndReplacements() {
        String input = "QuietCecilyCecilyQuietQuietQuietCecilyCecilyQuietQuietCecilyCecily";
        String expected = "QuietSiSiQuietSiSiQuietSiSi";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'QuietCecilyCecilyQuietQuietQuietCecilyCecilyQuietQuietCecilyCecily' should be 'QuietSiSiQuietSiSiQuietSiSi'", expected, actual);
    }

    @Test
    public void testValidStringWithHandlesAndReplacementsAndRemovals() {
        String input = "ThreesixteenTwentythreeThirtythreeTwentythreeThreesixteenThreesixteenTwentythreeThirtythreeTwentythreeThreesixteen";
        String expected = "ThreesixteenTwentythreeThreesixteen";
        String actual = encoder.encode(input);
        Assert.assertEquals("Caverphone of 'ThreesixteenTwentythreeThirtythreeTwentythreeThreesixteenThreesixteenTwentythreeThirtythreeTwentythreeThreesixteen' should be 'ThreesixteenTwentythreeThreesixteen'", expected, actual);
    }

    @Test
    public void testEqualStrings() {
        String str1 = "HelloWorld";
        String str2 = "HelloWorld";
        String actual = encoder.encode(str1);
        String expected = encoder.encode(str2);
        boolean result = encoder.isCaverphoneEqual(str1, str2);
        Assert.assertEquals("Caverphone of 'HelloWorld' should be equal for both strings", actual, expected);
        Assert.assertTrue("Caverphone of 'HelloWorld' should be equal for both strings according to isCaverphoneEqual()", result);
    }

    @Test
    public void testNotEqualStrings() {
        String str1 = "HelloWorld";
        String str2 = "HelloHello";
        String actual1 = encoder.encode(str1);
        String actual2 = encoder.encode(str2);
        String expected = encoder.encode("HelloWorld");
        boolean result = encoder.isCaverphoneEqual(str1, str2);
        Assert.assertNotEquals("Caverphone of 'HelloWorld' should not be equal to 'HelloHello'", actual1, actual2);
        Assert.assertFalse("Caverphone of 'HelloWorld' should not be equal to 'HelloHello' according to isCaverphoneEqual()", result);
        Assert.assertEquals("Caverphone of 'HelloWorld' should be equal to the caverphone of its own encoded value", expected, actual1);
    }

    @Test(expected = DecoderException.class)
    public void testInvalidStringLength() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        String expectedErrorMessage = "Parameter supplied to Caverphone encode is too long";

        try {
            encoder.encode(input.substring(0, input.length() + 1));
            Assert.fail("Expected DecoderException for string of incorrect length");
        } catch (DecoderException e) {
            Assert.assertEquals("Expected error message for string of incorrect length", expectedErrorMessage, e.getMessage());
        }
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInputChars() {
        String input = "HelloWorld!";
        String expectedErrorMessage = "Parameter supplied to Caverphone encode contains invalid characters";

        try {
            encoder.encode(input);
            Assert.fail("Expected EncoderException for input containing invalid characters");
        } catch (EncoderException e) {
            Assert.assertEquals("Expected error message for input containing invalid characters", expectedErrorMessage, e.getMessage());
        }
    }
}