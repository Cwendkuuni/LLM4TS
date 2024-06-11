import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    // Test for translate(CharSequence input, int index, Writer out)
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        try {
            StringBuilder output = new StringBuilder();
            int result = unescaper.translate("&#65;", 0, output);
            assertEquals(7, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }
}