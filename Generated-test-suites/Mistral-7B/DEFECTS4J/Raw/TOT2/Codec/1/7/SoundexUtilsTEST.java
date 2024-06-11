import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        String input = "aBc123dEf456gHiJk";
        String expected = "ABCDEFGHIJK";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    // Add test cases for edge cases and error scenarios here
}