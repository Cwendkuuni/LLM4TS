import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#65; &#x41; &#x01F5; &#10; &#x1F4; &#x1100;";
        Writer out = new java.io.StringWriter();
        int charsWritten = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("AB眅", out.getBuffer().toString());
        assertEquals(13, charsWritten);
    }

    // Test for edge cases and exceptions will be added in next steps
}