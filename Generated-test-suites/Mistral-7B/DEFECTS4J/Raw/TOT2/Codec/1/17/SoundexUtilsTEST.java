import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    @Test
    public void testCleanMethod() {
        String input = "This is a test string.";
        String expected = "This is a test string.";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = null;
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo wOrLd!";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }
}