import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("1111111111", caverphone.caverphone("12345"));

        assertEquals("F", caverphone.caverphone("foo"));
        assertEquals("F", caverphone.caverphone("FOO"));
        assertNotEquals("F", caverphone.caverphone("bar"));

        assertEquals("FSNT111111", caverphone.caverphone("far"));
        assertEquals("FSN1111111", caverphone.caverphone("fare"));

        // Test special characters
        assertEquals("FSN1111111", caverphone.caverphone("f@re"));
        assertEquals("2SN1111111", caverphone.caverphone("#fare"));
        assertEquals("2SN1111111", caverphone.caverphone("f!are"));
        assertEquals("FSNT111111", caverphone.caverphone("fa'r'e"));

        assertEquals("T", caverphone.caverphone("tough"));
        assertEquals("RT", caverphone.caverphone("rough"));
        assertEquals("CFSN111111", caverphone.caverphone("cough far"));
        assertEquals("TRSN111111", caverphone.caverphone("trough fare"));

        assertEquals("1231111111", caverphone.caverphone("123cough"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("F", caverphone.encode("foo"));
        assertEquals("RT", caverphone.encode("rough"));
        assertEquals("CFSN111111", caverphone.encode("cough far"));

        // Test non-string object
        try {
            caverphone.encode(new Object());
        } catch (EncoderException e) {
            assertTrue(true);
        }

        assertEquals("F", caverphone.encode((Object) "foo"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("foo", "FOO"));
        assertTrue(caverphone.isCaverphoneEqual("rough", "RUf"));
        assertFalse(caverphone.isCaverphoneEqual("foo", "bar"));
    }
}