package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test string with special characters
        assertEquals("1111111111", caverphone.caverphone("@#$%^&*()"));
        
        // Test string with numbers
        assertEquals("1111111111", caverphone.caverphone("12345"));
        
        // Test string with upper case letters
        assertEquals("KKKK111111", caverphone.caverphone("KAVA"));
        
        // Test regular string
        assertEquals("KMPTN11111", caverphone.caverphone("common"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws Exception {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(1234);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        // Test empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Test string with special characters
        assertEquals("1111111111", caverphone.encode("@#$%^&*()"));
        
        // Test string with numbers
        assertEquals("1111111111", caverphone.encode("12345"));
        
        // Test string with upper case letters
        assertEquals("KKKK111111", caverphone.encode("KAVA"));
        
        // Test regular string
        assertEquals("KMPTN11111", caverphone.encode("common"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equal caverphone codes
        assertTrue(caverphone.isCaverphoneEqual("common", "COMMON"));
        
        // Test different caverphone codes
        assertFalse(caverphone.isCaverphoneEqual("common", "cave"));
    }

}