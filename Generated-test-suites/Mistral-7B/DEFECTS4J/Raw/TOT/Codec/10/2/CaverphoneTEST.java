import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private static final String TEST_CASE_1 = "The quick Brown fox jumps over the lazy Dog";
    private static final String TEST_CASE_2 = "this is a Test String for Caverphone";
    private static final String TEST_CASE_3 = "12345";
    private static final String TEST_CASE_4 = "aAeiIoU";
    private static final String TEST_CASE_5 = "";
    private static final String TEST_CASE_6 = "Caverphone Test";

    @Test
    public void testCaverphoneEncodeAndDecode() {
        StringEncoder encoder = new Caverphone();
        String encodedTest1 = encoder.encode(TEST_CASE_1);
        String decodedTest1 = ((StringEncoder) encoder).decode(encodedTest1);
        String encodedTest2 = encoder.encode(TEST_CASE_2);
        String decodedTest2 = ((StringEncoder) encoder).decode(encodedTest2);

        Assert.assertEquals(TEST_CASE_1, decodedTest1);
        Assert.assertEquals(TEST_CASE_2, decodedTest2);
    }

    @Test
    public void testCaverphoneEncodeEmptyString() {
        StringEncoder encoder = new Caverphone();
        String encodedEmptyString = encoder.encode(TEST_CASE_5);

        Assert.assertEquals("1111111111", encodedEmptyString);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneEncodeNullString() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(null);
    }

    @Test
    public void testCaverphoneEncodeSpecialChars() {
        StringEncoder encoder = new Caverphone();
        String encodedTest6 = encoder.encode(TEST_CASE_6);

        Assert.assertEquals("T3h3 q3u3i2 c4m5m5 p3s3p3 s2 03e2 t3s3 32e3 32t3 03e2 k3p3 33s3 33p3 k3f3 33m3 33n3", encodedTest6);
    }

    @Test
    public void testCaverphoneEncodeLongString() {
        String longString = StringUtils.repeat("a", 256);
        StringEncoder encoder = new Caverphone();
        String encodedLongString = encoder.encode(longString);

        // As per the documentation, the encoded string should have a length of 512 + 10.
        Assert.assertEquals(522, encodedLongString.length());
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        StringEncoder encoder = new Caverphone();
        String encodedTest1 = encoder.encode(TEST_CASE_1);
        String encodedTest2 = encoder.encode(TEST_CASE_1);

        Assert.assertTrue(encoder.isCaverphoneEqual(TEST_CASE_1, TEST_CASE_1));
        Assert.assertTrue(encoder.isCaverphoneEqual(TEST_CASE_1, TEST_CASE_1));
        Assert.assertFalse(encoder.isCaverphoneEqual(TEST_CASE_1, TEST_CASE_2));
        Assert.assertFalse(encoder.isCaverphoneEqual(TEST_CASE_2, TEST_CASE_1));
    }

    @Test(expected = DecoderException.class)
    public void testCaverphoneDecodeInvalidCode() {
        StringEncoder encoder = new Caverphone();
        String invalidCode = "12345678901234567890123456"; // invalid code length
        ((StringEncoder) encoder).decode(invalidCode);
    }
}