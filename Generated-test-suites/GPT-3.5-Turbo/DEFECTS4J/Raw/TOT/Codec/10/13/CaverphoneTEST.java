import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_emptyString() {
        String result = caverphone.caverphone("");

        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercase() {
        String result = caverphone.caverphone("HELLO");

        Assert.assertEquals("HL1111", result);
    }

    @Test
    public void testCaverphone_removeNonAlphabetic() {
        String result = caverphone.caverphone("hello123");

        Assert.assertEquals("HL1111", result);
    }

    @Test
    public void testCaverphone_removeFinalE() {
        String result = caverphone.caverphone("apple");

        Assert.assertEquals("AP11111111", result);
    }

    @Test
    public void testCaverphone_startOptions() {
        String result1 = caverphone.caverphone("coughtest");
        String result2 = caverphone.caverphone("toughcookie");
        String result3 = caverphone.caverphone("gnome");

        Assert.assertEquals("CA2T2ST", result1);
        Assert.assertEquals("TA2KI", result2);
        Assert.assertEquals("N2M", result3);
    }

    @Test
    public void testCaverphone_replacements() {
        String result1 = caverphone.caverphone("clock");
        String result2 = caverphone.caverphone("tch");
        String result3 = caverphone.caverphone("time");

        Assert.assertEquals("KLOK", result1);
        Assert.assertEquals("2K", result2);
        Assert.assertEquals("SA2", result3);
    }

    @Test
    public void testCaverphone_handleRemovals() {
        String result = caverphone.caverphone("few");

        Assert.assertEquals("F2", result);
    }

    @Test
    public void testCaverphone_tenOnEnd() {
        String result = caverphone.caverphone("testing");

        Assert.assertEquals("TAS2N11111", result);
    }

    @Test
    public void testEncode_validString() {
        String result = caverphone.encode("apple");

        Assert.assertEquals("AP11111111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_wrongType() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testEncodeNullValue() {
        String result = caverphone.encode(null);

        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        boolean result = caverphone.isCaverphoneEqual("apple", "apple");

        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        boolean result = caverphone.isCaverphoneEqual("apple", "orange");

        Assert.assertFalse(result);
    }
}