import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone c = new Caverphone();
        assertEquals("1111111111", c.caverphone(null));
        assertEquals("1111111111", c.caverphone(""));
        assertEquals("s111111111", c.caverphone("s"));
        assertEquals("sw11111111", c.caverphone("sw"));
        assertEquals("swt1111111", c.caverphone("swt"));
        assertEquals("swyt111111", c.caverphone("swyt"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone c = new Caverphone();
        try {
            c.encode(new Object());
            assertFalse(true);
        } catch (EncoderException e) {
            assertTrue(true);
        }
        assertEquals("1111111111", c.encode(null));
        assertEquals("1111111111", c.encode(""));
        assertEquals("s111111111", c.encode("s"));
        assertEquals("sw11111111", c.encode("sw"));
        assertEquals("swt1111111", c.encode("swt"));
        assertEquals("swyt111111", c.encode("swyt"));
    }

    @Test
    public void testEncodeString() {
        Caverphone c = new Caverphone();
        assertEquals("1111111111", c.encode((String)null));
        assertEquals("1111111111", c.encode(""));
        assertEquals("s111111111", c.encode("s"));
        assertEquals("sw11111111", c.encode("sw"));
        assertEquals("swt1111111", c.encode("swt"));
        assertEquals("swyt111111", c.encode("swyt"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone c = new Caverphone();
        assertTrue(c.isCaverphoneEqual(null, null));
        assertTrue(c.isCaverphoneEqual("", ""));
        assertTrue(c.isCaverphoneEqual("s", "S"));
        assertTrue(c.isCaverphoneEqual("sw", "SW"));
        assertTrue(c.isCaverphoneEqual("swt", "SWT"));
        assertTrue(c.isCaverphoneEqual("swyt", "SWYT"));
        assertFalse(c.isCaverphoneEqual("s", "s1"));
        assertFalse(c.isCaverphoneEqual("sw1", "swyt"));
        assertFalse(c.isCaverphoneEqual("swyt", "swyt1"));
    }
}