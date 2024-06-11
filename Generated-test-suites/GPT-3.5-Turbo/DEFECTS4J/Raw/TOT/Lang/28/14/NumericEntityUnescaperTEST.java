import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_WhenValidNumericEntity_ReturnsTranslatedCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "&#65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(2, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WhenInvalidNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "&#invalid;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WhenHexNumericEntity_ReturnsTranslatedCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "&#x41;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(4, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WhenNumericEntityInMiddle_ReturnsTranslatedCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "Text&#80;RemainingText";
        int index = 4;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(4, result);
        Assert.assertEquals("P", writer.toString());
    }

    @Test
    public void testTranslate_WhenNumericEntityAtEnd_ReturnsTranslatedCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "Text&#80;";
        int index = 4;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(4, result);
        Assert.assertEquals("P", writer.toString());
    }

    @Test
    public void testTranslate_WhenNoNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        CharSequence input = "Text";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }
}