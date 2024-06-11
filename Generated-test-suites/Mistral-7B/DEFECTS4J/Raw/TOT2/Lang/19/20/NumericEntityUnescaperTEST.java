import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#65; &#x41; &#123; &amp;#160; &#xFFFF; &amp;#x1F30A;";
        String expectedOutput = "AB &A a { ␠";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(expectedOutput, out.toString().substring(0, length));
    }
}