import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String testCases = "&amp;#160; &#123; &#65;&#97;&#110; "
                + "&#x416; &#x1F600; &#xFFFF; "
                + "&amp;#xDEADBEEF; &amp;#x0; &amp;#x10FFFF; ";

        for (String testCase : testCases.split(" ")) {
            int length = unescaper.translate(testCase, 0, out);
            String expected = testCase.replaceAll("&[^;]*;", ""); // Remove entities
            String actual = out.toString().substring(0, length);

            assertEquals(expected, actual);
        }
    }

    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&amp;#xDEADBEEF; &amp;#x0; &amp;#x10FFFF";

        unescaper.translate(input, 0, out); // Intentionally not closing the Writer
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&amp;#xABC;"; // Invalid hex entity

        unescaper.translate(input, 0, out);
    }
}