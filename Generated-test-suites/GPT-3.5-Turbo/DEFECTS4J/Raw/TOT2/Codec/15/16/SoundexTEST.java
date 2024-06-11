import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        String input1 = "John";
        String input2 = "Jon";
        int expected = 4;
        int result = soundex.difference(input1, input2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        String input = "Robert";
        String expected = "R163";
        Object result = soundex.encode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "Michael";
        String expected = "M240";
        String result = soundex.encode(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String input = "Alice";
        String expected = "A420";
        String result = soundex.soundex(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMaxLength() {
        Soundex soundex = new Soundex();
        int expected = 4;
        int result = soundex.getMaxLength();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        int expected = 5;
        soundex.setMaxLength(5);
        int result = soundex.getMaxLength();
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodeException() {
        Soundex soundex = new Soundex();
        Object input = 123; // Non-string input
        assertThrows(EncoderException.class, () -> {
            soundex.encode(input);
        });
    }
    
    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] expected = Soundex.US_ENGLISH_MAPPING;
        char[] result = soundex.getSoundexMapping();
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCustomSoundexMapping() {
        char[] customMapping = {'0','1','2','3','4','5','6','7','8','9'};
        Soundex soundex = new Soundex(customMapping);
        String input = "Custom";
        String expected = "C123";
        String result = soundex.soundex(input);
        assertEquals(expected, result);
    }
    
}