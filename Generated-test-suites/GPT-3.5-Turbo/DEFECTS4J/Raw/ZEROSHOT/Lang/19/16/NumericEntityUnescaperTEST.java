import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test with input "&#x41;" (hexadecimal)
        CharSequence input = "&#x41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(6, result);
        Assert.assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);

        // Test with input "&#65;" (decimal)
        input = "&#65;";
        result = unescaper.translate(input, index, writer);

        Assert.assertEquals(5, result);
        Assert.assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);

        // Test with input "&amp;amp;"
        input = "&amp;amp;";
        result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

}