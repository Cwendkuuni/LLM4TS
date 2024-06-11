import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("Test"));
        assertEquals("A11111A111", caverphone.caverphone("A Test String"));
        assertEquals("DvA3111A111", caverphone.caverphone("Dávid Test String"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObject_Exception() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("A11111A111", caverphone.encode("A Test String"));
        assertEquals("DvA3111A111", caverphone.encode("Dávid Test String"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode((String) null));
        assertEquals("A11111A111", caverphone.encode("A Test String"));
        assertEquals("DvA3111A111", caverphone.encode("Dávid Test String"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, "Test"));
        assertFalse(caverphone.isCaverphoneEqual("Test", null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Test", "Test"));
        assertTrue(caverphone.isCaverphoneEqual("A Test String", "A Test String"));
        assertTrue(caverphone.isCaverphoneEqual("Dávid Test String", "Dávid Test String"));
    }

}