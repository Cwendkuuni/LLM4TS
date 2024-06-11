import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160;&#163;&#x20AC;&#x2014;";
        Writer out = new StringWriter();
        int index = 0;

        int actualLength = unescaper.translate(new StringReader(input), index, out);
        String expectedOutput = " —\u00A3";
        String actualOutput = out.toString();

        assertEquals(expectedOutput.length(), actualLength);
        assertEquals(expectedOutput, actualOutput);
    }
}