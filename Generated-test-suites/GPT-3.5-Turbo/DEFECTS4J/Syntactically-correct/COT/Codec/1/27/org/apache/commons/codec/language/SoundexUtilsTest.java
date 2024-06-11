import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        // Test input with only upper case letters
        String input1 = "HELLO WORLD";
        String expected1 = "HELLO WORLD";
        assertEquals(expected1, SoundexUtils.clean(input1));

        // Test input with mixed case letters
        String input2 = "HeLlO wOrLd";
        String expected2 = "HELLO WORLD";
        assertEquals(expected2, SoundexUtils.clean(input2));

        // Test input with non-letter characters
        String input3 = "Hello123World!";
        String expected3 = "HELLO WORLD";
        assertEquals(expected3, SoundexUtils.clean(input3));

        // Test null input
        String input4 = null;
        assertNull(SoundexUtils.clean(input4));

        // Test empty input
        String input5 = "";
        assertEquals("", SoundexUtils.clean(input5));
    }
}