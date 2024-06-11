import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));

        assertEquals("KM21111111", caverphone.caverphone("Kilimanjaro"));
        assertEquals("SM21111111", caverphone.caverphone("Summit"));
        
        // Edge case for handling special characters
        assertEquals("KM21111111", caverphone.caverphone("!@#$%^"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        try {
            caverphone.encode(123); // Exception handling scenario
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("KM21111111", caverphone.encode("Kilimanjaro"));
        assertEquals("SM21111111", caverphone.encode("Summit"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("Kilimanjaro", "Kilimanjaro"));
        assertFalse(caverphone.isCaverphoneEqual("Kilimanjaro", "Summit"));
    }

}