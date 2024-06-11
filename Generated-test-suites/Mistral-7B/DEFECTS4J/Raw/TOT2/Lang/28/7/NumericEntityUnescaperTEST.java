import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.StringWriter;
import static org.apache.commons.lang3.CharSequenceUtils.subsequence;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String testInput = "&amp;#160;&#x20AC;&#x2013;";
        StringWriter out = new StringWriter();

        int len = unescaper.translate(new StringReader(testInput), 0, out);
        String expectedOutput = "\u00A0\u20AC--";
        assertEquals(expectedOutput, out.toString().substring(0, len));
    }
}