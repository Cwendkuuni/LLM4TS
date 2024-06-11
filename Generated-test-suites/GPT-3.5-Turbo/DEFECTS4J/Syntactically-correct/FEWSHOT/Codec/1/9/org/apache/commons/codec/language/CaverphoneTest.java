
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.caverphone(null));
        assertEquals("1111111111", encoder.caverphone(""));
        assertEquals("av11111111", encoder.caverphone("A"));
        assertEquals("11r1111111", encoder.caverphone("R"));
    }
    
    @Test
    public void testEncode() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.encode(null));
        assertEquals("1111111111", encoder.encode(""));
        assertEquals("av11111111", encoder.encode("A"));
        assertEquals("11r1111111", encoder.encode("R"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        boolean result1 = encoder.isCaverphoneEqual(null, null);
        boolean result2 = encoder.isCaverphoneEqual("", "");
        assertEquals(result1, result2);
        
        boolean result3 = encoder.isCaverphoneEqual("A", "A");
        boolean result4 = encoder.isCaverphoneEqual("R", "R");
        assertEquals(result3, result4);
        
        boolean result5 = encoder.isCaverphoneEqual("A", "R");
        boolean result6 = encoder.isCaverphoneEqual("R", "A");
        assertNotEquals(result5, result6);
    }
}
