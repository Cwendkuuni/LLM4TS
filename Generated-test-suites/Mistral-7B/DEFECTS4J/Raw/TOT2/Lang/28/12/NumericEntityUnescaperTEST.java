import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#160; &#x20AC; &#123; &#xFFFC; &#x1F600;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, output);

        String expectedOutput = "␠ € ₦ ﬂ 🔟";
        assertEquals(expectedOutput, output.toString().substring(0, length));
    }
}