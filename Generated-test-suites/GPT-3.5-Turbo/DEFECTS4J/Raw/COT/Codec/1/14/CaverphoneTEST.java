import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.*;

public class CaverphoneTest {
    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("554237", caverphone.caverphone("Apache Software Foundation"));
        assertEquals("KR123", caverphone.caverphone("Kevin Rodriguez"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("554237", caverphone.encode("Apache Software Foundation"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidType() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testEncodeString() {
        assertEquals("554237", caverphone.encode("Apache Software Foundation"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Apache", "Apache"));
        assertFalse(caverphone.isCaverphoneEqual("Apache", "Software"));
    }
}