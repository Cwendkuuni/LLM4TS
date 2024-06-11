import org.junit.*;
import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test case 1: Testing two strings with 4 similar characters
        String s1 = "ABCD";
        String s2 = "AQWD";
        int expected1 = 4;
        int result1 = soundex.difference(s1, s2);
        assertEquals(expected1, result1);
        
        // Test case 2: Testing two strings with 1 similar character
        String s3 = "QWERTY";
        String s4 = "ASDFGH";
        int expected2 = 1;
        int result2 = soundex.difference(s3, s4);
        assertEquals(expected2, result2);
        
        // Test case 3: Testing two empty strings
        String s5 = "";
        String s6 = "";
        int expected3 = 0;
        int result3 = soundex.difference(s5, s6);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test case 1: Testing encoding of a string
        String input1 = "APPLE";
        String expected1 = "A140";
        String result1 = soundex.encode(input1);
        assertEquals(expected1, result1);
        
        // Test case 2: Testing encoding of an empty string
        String input2 = "";
        String expected2 = "";
        String result2 = soundex.encode(input2);
        assertEquals(expected2, result2);
        
        // Test case 3: Testing encoding of a string containing special characters
        String input3 = "A!@#$%^&*()";
        String expected3 = "A000";
        String result3 = soundex.encode(input3);
        assertEquals(expected3, result3);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_InvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test case: Testing encoding of an invalid input
        Object input4 = new Object();
        soundex.encode(input4);
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        // Test case 1: Testing Soundex encoding of a string
        String input1 = "HELLO";
        String expected1 = "H400";
        String result1 = soundex.soundex(input1);
        assertEquals(expected1, result1);
        
        // Test case 2: Testing Soundex encoding of an empty string
        String input2 = "";
        String expected2 = "";
        String result2 = soundex.soundex(input2);
        assertEquals(expected2, result2);
        
        // Test case 3: Testing Soundex encoding of a string containing special characters
        String input3 = "!@#$%^&*()";
        String expected3 = "0000";
        String result3 = soundex.soundex(input3);
        assertEquals(expected3, result3);
    }
    
}