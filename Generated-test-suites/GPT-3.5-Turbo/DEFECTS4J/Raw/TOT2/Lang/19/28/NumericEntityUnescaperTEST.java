import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate() throws IOException {
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, new StringWriter(output));
        assertEquals(5, output.length());
        assertEquals('A', output.charAt(0));
        assertEquals(5, result);
    }

    @Test
    public void testTranslateWithHex() throws IOException {
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x99;", 0, new StringWriter(output));
        assertEquals(1, output.length());
        assertEquals('\u0099', output.charAt(0));
        assertEquals(6, result);
    }

    @Test
    public void testTranslateEdgeCase() throws IOException {
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65536;", 0, new StringWriter(output));
        assertEquals(1, output.length());
        assertEquals('\uD800', output.charAt(0));
        assertEquals(9, result);
    }

    @Test
    public void testTranslateNumberFormatException() throws IOException {
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#invalid;", 0, new StringWriter(output));
        assertEquals(0, output.length());
        assertEquals(0, result);
    }

}