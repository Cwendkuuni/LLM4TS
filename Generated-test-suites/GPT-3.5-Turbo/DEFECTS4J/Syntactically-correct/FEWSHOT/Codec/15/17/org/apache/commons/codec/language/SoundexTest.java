package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
        
        Soundex soundexWithMapping = new Soundex(new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
        assertNotNull(soundexWithMapping);
        
        Soundex soundexWithMappingString = new Soundex("0123456789");
        assertNotNull(soundexWithMappingString);
    }
    
    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        
        try {
            soundex.encode(new Integer(123));
            fail("IllegalArgumentException should be thrown when encoding non-String object");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("not of type java.lang.String"));
        }
    }
    
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        String encoded = soundex.encode("Smith");
        assertEquals("S530", encoded);
        
        String nullEncoded = soundex.encode(null);
        assertNull(nullEncoded);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        int diff = soundex.difference("Test1", "Test2");
        assertEquals(3, diff);
        
        try {
            soundex.difference(null, "Test");
            fail("EncoderException should be thrown when one of the parameters is null");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("error occurs encoding one of the strings"));
        }
    }
    
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        
        char[] mapping = soundex.getSoundexMapping();
        assertEquals('0', mapping[0]);
        assertEquals('1', mapping[1]);
        assertEquals('2', mapping[2]);
        assertEquals('3', mapping[3]);
        assertEquals('4', mapping[4]);
        assertEquals('5', mapping[5]);
        assertEquals('6', mapping[6]);
        assertEquals('7', mapping[7]);
        assertEquals('8', mapping[8]);
        assertEquals('9', mapping[9]);
    }
    
    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        
        char code = soundex.getMappingCode("Test", 0);
        assertEquals('T', code);
        
        try {
            soundex.getMappingCode("Test", 4);
            fail("IllegalArgumentException should be thrown when character is not mapped");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("The character is not mapped"));
        }
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        
        soundex.setMaxLength(5);
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        String encoded = soundex.soundex("Smith");
        assertEquals("S530", encoded);
        
        String nullEncoded = soundex.soundex(null);
        assertNull(nullEncoded);
    }
}