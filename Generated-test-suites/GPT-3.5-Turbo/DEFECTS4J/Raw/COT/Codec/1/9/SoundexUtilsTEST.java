import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexUtilsTest {
  
    @Test
    public void testClean() {
        String input = "abc123";
        String expectedOutput = "ABC";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
        
        input = "Hello World";
        expectedOutput = "HELLOWORLD";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
        
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
        
        input = null;
        assertNull(SoundexUtils.clean(input));
    }
  
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        
        String s1 = "hello";
        String s2 = "HEILLO";
        int expectedOutput = 3;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "world";
        s2 = "WORLD";
        expectedOutput = 4;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "test";
        s2 = "TESTING";
        expectedOutput = 3;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "one";
        s2 = "two";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "";
        s2 = "empty";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = null;
        s2 = "null";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
    }
  
    @Test
    public void testDifferenceEncoded() {
        String es1 = "H400";
        String es2 = "H401";
        int expectedOutput = 2;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = "W300";
        es2 = "W400";
        expectedOutput = 1;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = "";
        es2 = "empty";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = null;
        es2 = "null";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
    }
  
    // Additional test cases for potential edge cases and exception handling scenarios can be added here
  
}



// Include the above test methods here

