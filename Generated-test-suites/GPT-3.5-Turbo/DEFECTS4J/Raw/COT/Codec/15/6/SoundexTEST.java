import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test case 1: Both strings are empty
        String s1 = "";
        String s2 = "";
        int expected1 = 0;
        int result1 = soundex.difference(s1, s2);
        assertEquals(expected1, result1);
        
        // Test case 2: Both strings have different characters
        String s3 = "abcd";
        String s4 = "efgh";
        int expected2 = 0;
        int result2 = soundex.difference(s3, s4);
        assertEquals(expected2, result2);
        
        // Test case 3: Strings have some similar characters
        String s5 = "abcde";
        String s6 = "abfgh";
        int expected3 = 2;
        int result3 = soundex.difference(s5, s6);
        assertEquals(expected3, result3);
        
        // Test case 4: Strings have all similar characters
        String s7 = "abcdef";
        String s8 = "abcdef";
        int expected4 = 4;
        int result4 = soundex.difference(s7, s8);
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test case 1: Empty string
        String str1 = "";
        String expected1 = "0000";
        String result1 = soundex.encode(str1);
        assertEquals(expected1, result1);
        
        // Test case 2: String with all unique characters
        String str2 = "abcd";
        String expected2 = "A123";
        String result2 = soundex.encode(str2);
        assertEquals(expected2, result2);
        
        // Test case 3: String with repeating characters
        String str3 = "aabb";
        String expected3 = "A100";
        String result3 = soundex.encode(str3);
        assertEquals(expected3, result3);
        
        // Test case 4: String with leading and trailing whitespace
        String str4 = " abcd ";
        String expected4 = "A123";
        String result4 = soundex.encode(str4);
        assertEquals(expected4, result4);
    }
    
}