import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    Caverphone caverphone;

    @Before
    public void setup(){
        caverphone = new Caverphone();
    }

    @After
    public void tearDown(){
        caverphone = null;
    }

    @Test
    public void testCaverphone_ValidString_ReturnsCaverphoneCode() {
        String input = "hello";
        String expectedOutput = "HLL1111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_EmptyString_ReturnsDefaultCaverphoneCode() {
        String input = "";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_NullString_ReturnsDefaultCaverphoneCode() {
        String input = null;
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_EdgeCaseStartOptions_ReturnsCaverphoneCode() {
        String input = "coughtest";
        String expectedOutput = "KFT1111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_EdgeCaseFinalE_ReturnsCaverphoneCode() {
        String input = "smile";
        String expectedOutput = "SMLA111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_EdgeCaseReplacements_ReturnsCaverphoneCode() {
        String input = "coeur";
        String expectedOutput = "K3R1111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode_ValidString_ReturnsCaverphoneCodeAsObject() throws EncoderException {
        String input = "hello";
        String expectedOutput = "HLL1111111";
        Object actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_InvalidType_ThrowsEncoderException() throws EncoderException {
        Integer input = 123;
        caverphone.encode(input);
    }

    @Test
    public void testEncode_ValidString_ReturnsCaverphoneCodeAsString() throws EncoderException {
        String input = "hello";
        String expectedOutput = "HLL1111111";
        String actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsCaverphoneEqual_IdenticalStrings_ReturnsTrue() {
        String str1 = "hello";
        String str2 = "hello";
        boolean expectedOutput = true;
        boolean actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStrings_ReturnsFalse() {
        String str1 = "hello";
        String str2 = "world";
        boolean expectedOutput = false;
        boolean actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

}