import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "A an Apple a day keeps the Doctor away!";
        String expected = "A A A A A A A A A";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        input = "1234abcdE5F6";
        result = SoundexUtils.clean(input);
        Assert.assertEquals("", result);

        input = null;
        result = SoundexUtils.clean(input);
        Assert.assertEquals("", result);
    }

    @Test
    public void testDifferenceEncoded() {
        String soundex1 = Soundex.soundex("Apple");
        String soundex2 = Soundex.soundex("Orange");
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(soundex1, soundex2);
        Assert.assertEquals(expected, result);

        soundex1 = Soundex.soundex("Apple");
        soundex2 = Soundex.soundex("Apple");
        expected = 4;
        result = SoundexUtils.differenceEncoded(soundex1, soundex2);
        Assert.assertEquals(expected, result);

        soundex1 = Soundex.soundex("Apple");
        soundex2 = Soundex.soundex("Orangutan");
        expected = 3;
        result = SoundexUtils.differenceEncoded(soundex1, soundex2);
        Assert.assertEquals(expected, result);

        soundex1 = Soundex.soundex("Apple");
        soundex2 = Soundex.soundex("Apple sauce");
        expected = 3;
        result = SoundexUtils.differenceEncoded(soundex1, soundex2);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedInvalidInput() {
        Soundex soundex = new Soundex();
        String input1 = "Apple";
        String input2 = null;
        int expected = 0;
        try {
            int result = SoundexUtils.differenceEncoded(soundex.encode(input1), soundex.encode(input2));
            Assert.fail("Expected EncoderException but test passed.");
        } catch (EncoderException e) {
            // expected
        }
        Assert.assertNotEquals(expected, SoundexUtils.differenceEncoded(input1, input2));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedInvalidInput2() {
        Soundex soundex = new Soundex();
        String input1 = null;
        String input2 = "Apple";
        int expected = 0;
        try {
            int result = SoundexUtils.differenceEncoded(soundex.encode(input1), soundex.encode(input2));
            Assert.fail("Expected EncoderException but test passed.");
        } catch (EncoderException e) {
            // expected
        }
        Assert.assertNotEquals(expected, SoundexUtils.differenceEncoded(input1, input2));
    }

    @Test(expected = DecoderException.class)
    public void testDifferenceEncodedInvalidInput3() {
        Soundex soundex = new Soundex();
        String input1 = "Apple";
        String input2 = null;
        int expected = 0;
        String decodedInput1 = soundex.decode("AP130");
        String decodedInput2 = null;
        try {
            int result = SoundexUtils.differenceEncoded(decodedInput1, decodedInput2);
            Assert.fail("Expected DecoderException but test passed.");
        } catch (DecoderException e) {
            // expected
        }
        Assert.assertNotEquals(expected, SoundexUtils.differenceEncoded(decodedInput1, decodedInput2));
    }

    @Test(expected = DecoderException.class)
    public void testDifferenceEncodedInvalidInput4() {
        Soundex soundex = new Soundex();
        String input1 = null;
        String input2 = "Apple";
        int expected = 0;
        String decodedInput1 = null;
        String decodedInput2 = soundex.decode("AP130");
        try {
            int result = SoundexUtils.differenceEncoded(decodedInput1, decodedInput2);
            Assert.fail("Expected DecoderException but test passed.");
        } catch (DecoderException e) {
            // expected
        }
        Assert.assertNotEquals(expected, SoundexUtils.differenceEncoded(decodedInput1, decodedInput2));
    }
}