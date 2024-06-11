
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone c = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", c.caverphone(""));
        
        // Test lowercase input
        assertEquals("1111111111", c.caverphone("abcdefghijklmnopqrstuvwxyz"));
        
        // Test uppercase input
        assertEquals("1111111111", c.caverphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        
        // Test input with numbers
        assertEquals("1111111111", c.caverphone("1234567890"));
        
        // Test input with special characters
        assertEquals("1111111111", c.caverphone("!@#$%^&*()_-+=[]{}|;':,.<>?/"));
        
        // Test input with spaces
        assertEquals("1111111111", c.caverphone("This is a test"));
        
        // Test input with non-alphabetical characters removed
        assertEquals("WDN1111111", c.caverphone("W$dN!@#"));
    }
    
    @Test
    public void testEncode() {
        Caverphone c = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", c.encode(""));
        
        // Test lowercase input
        assertEquals("1111111111", c.encode("abcdefghijklmnopqrstuvwxyz"));
        
        // Test uppercase input
        assertEquals("1111111111", c.encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        
        // Test input with numbers
        assertEquals("1111111111", c.encode("1234567890"));
        
        // Test input with special characters
        assertEquals("1111111111", c.encode("!@#$%^&*()_-+=[]{}|;':,.<>?/"));
        
        // Test input with spaces
        assertEquals("1111111111", c.encode("This is a test"));
        
        // Test input with non-alphabetical characters removed
        assertEquals("WDN1111111", c.encode("W$dN!@#"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone c = new Caverphone();
        
        // Test same strings
        assertEquals(true, c.isCaverphoneEqual("hello", "hello"));
        
        // Test different strings
        assertEquals(false, c.isCaverphoneEqual("hello", "world"));
        
        // Test strings with different cases
        assertEquals(true, c.isCaverphoneEqual("HELLO", "hello"));
        
        // Test empty string
        assertEquals(true, c.isCaverphoneEqual("", ""));
    }

}
