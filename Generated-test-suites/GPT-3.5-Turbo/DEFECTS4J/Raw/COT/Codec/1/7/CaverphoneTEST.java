import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {
    
    // Test caverphone method
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("c111111111", caverphone.caverphone("Cough"));
        assertEquals("wN11111111", caverphone.caverphone("Wrought"));
        assertEquals("A111111111", caverphone.caverphone("Enough"));
    }
    
    // Test encode method with EncoderException
    @Test(expected = EncoderException.class)
    public void testEncodeWithEncoderException() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }
    
    // Test encode method
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("c111111111", caverphone.encode("Cough"));
        assertEquals("wN11111111", caverphone.encode("Wrought"));
        assertEquals("A111111111", caverphone.encode("Enough"));
    }
    
    // Test isCaverphoneEqual method
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("Cough", "Cuff"));
        assertFalse(caverphone.isCaverphoneEqual("Wrought", "Write"));
    }
}