import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test with empty string
        Assert.assertEquals("1111111111", caverphone.caverphone(""));

        // Test with lowercase string
        Assert.assertEquals("kma1111111", caverphone.caverphone("Kumar"));

        // Test with uppercase string
        Assert.assertEquals("kma1111111", caverphone.caverphone("KUMAR"));

        // Test with string containing special characters and numbers
        Assert.assertEquals("kma1111111", caverphone.caverphone("Kuma$r234"));

        // Test with string containing only special characters and numbers
        Assert.assertEquals("1111111111", caverphone.caverphone("$!@#123456"));

        // Test with string containing only vowels
        Assert.assertEquals("fmm1111111", caverphone.caverphone("aeiou"));

        // Test with string containing only consonants
        Assert.assertEquals("syktn12111", caverphone.caverphone("bcdfg"));

        // Test with string containing all characters from a-z
        Assert.assertEquals("fkms2n2ctp", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));

        // Test with string containing all characters from A-Z
        Assert.assertEquals("fkms2n2ctp", caverphone.caverphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        // Test with string containing mix of lowercase and uppercase characters
        Assert.assertEquals("fmsk2n2ctp", caverphone.caverphone("AbCDefGHIjKLMnopQRstUvWXYZ"));

        // Test with string containing only numbers
        Assert.assertEquals("1111111111", caverphone.caverphone("1234567890"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        // Test with empty string
        Assert.assertEquals("1111111111", caverphone.encode(""));

        // Test with lowercase string
        Assert.assertEquals("kma1111111", caverphone.encode("Kumar"));

        // Test with uppercase string
        Assert.assertEquals("kma1111111", caverphone.encode("KUMAR"));

        // Test with string containing special characters and numbers
        Assert.assertEquals("kma1111111", caverphone.encode("Kuma$r234"));

        // Test with string containing only special characters and numbers
        Assert.assertEquals("1111111111", caverphone.encode("$!@#123456"));

        // Test with string containing only vowels
        Assert.assertEquals("fmm1111111", caverphone.encode("aeiou"));

        // Test with string containing only consonants
        Assert.assertEquals("syktn12111", caverphone.encode("bcdfg"));

        // Test with string containing all characters from a-z
        Assert.assertEquals("fkms2n2ctp", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));

        // Test with string containing all characters from A-Z
        Assert.assertEquals("fkms2n2ctp", caverphone.encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        // Test with string containing mix of lowercase and uppercase characters
        Assert.assertEquals("fmsk2n2ctp", caverphone.encode("AbCDefGHIjKLMnopQRstUvWXYZ"));

        // Test with string containing only numbers
        Assert.assertEquals("1111111111", caverphone.encode("1234567890"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test with equal strings
        Assert.assertTrue(caverphone.isCaverphoneEqual("Kumar", "Kumar"));

        // Test with unequal strings
        Assert.assertFalse(caverphone.isCaverphoneEqual("Kumar", "Kumari"));
    }
}