import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Typical use case
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, output);
        assertEquals("Translation for '&#65;' should be 'A'", 'A', output.charAt(0));
        assertEquals("Index should be incremented by 4", 4, result);

        // Edge case for hex entity value
        output.setLength(0);
        result = unescaper.translate("&#x1F600;", 0, output);
        assertEquals("Translation for '&#x1F600;' should be '\uD83D\uDE00'", "\uD83D\uDE00", output.toString());
        assertEquals("Index should be incremented by 9", 9, result);

        // Edge case for entity value greater than 0xFFFF
        output.setLength(0);
        result = unescaper.translate("&#128514;", 0, output);
        assertEquals("Translation for '&#128514;' should be '\uD83D\uDE00'", "\uD83D\uDE00", output.toString());
        assertEquals("Index should be incremented by 8", 8, result);

        // Error scenario for invalid numeric entity
        output.setLength(0);
        result = unescaper.translate("&#invalid;", 0, output);
        assertEquals("Output should be empty for invalid numeric entity", 0, output.length());
        assertEquals("Index should not be incremented", 0, result);
    }
}