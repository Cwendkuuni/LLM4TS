import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withValidNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        CharSequence input = "I have a numeric entity: &#65;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 20, writer);

        Assert.assertEquals("Translation is incorrect", 1, result);
        Assert.assertEquals("Writer output is incorrect", "I have a numeric entity: A", writer.toString());
    }

    @Test
    public void testTranslate_withValidHexadecimalNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        CharSequence input = "I have a hexadecimal numeric entity: &#x41;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 31, writer);

        Assert.assertEquals("Translation is incorrect", 1, result);
        Assert.assertEquals("Writer output is incorrect", "I have a hexadecimal numeric entity: A", writer.toString());
    }

    @Test
    public void testTranslate_withValidNumericEntityWithoutSemiColon_shouldReturnCorrectTranslation() throws IOException {
        CharSequence input = "I have a numeric entity without semi-colon: &#65";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 35, writer);

        Assert.assertEquals("Translation is incorrect", 0, result);
        Assert.assertEquals("Writer output is incorrect", "", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldReturnZero() throws IOException {
        CharSequence input = "I have an invalid numeric entity: &#invalid;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 27, writer);

        Assert.assertEquals("Translation is incorrect", 0, result);
        Assert.assertEquals("Writer output is incorrect", "", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexadecimalNumericEntity_shouldReturnZero() throws IOException {
        CharSequence input = "I have an invalid hexadecimal numeric entity: &#xinvalid;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 40, writer);

        Assert.assertEquals("Translation is incorrect", 0, result);
        Assert.assertEquals("Writer output is incorrect", "", writer.toString());
    }

    @Test
    public void testTranslate_withOutOfRangeNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        CharSequence input = "I have an out of range numeric entity: &#65536;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 35, writer);

        Assert.assertEquals("Translation is incorrect", 6, result);
        Assert.assertEquals("Writer output is incorrect", "", writer.toString());
    }

    @Test
    public void testTranslate_withOutOfRangeHexadecimalNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        CharSequence input = "I have an out of range hexadecimal numeric entity: &#x10000;";
        StringWriter writer = new StringWriter();

        int result = numericEntityUnescaper.translate(input, 46, writer);

        Assert.assertEquals("Translation is incorrect", 8, result);
        Assert.assertEquals("Writer output is incorrect", "", writer.toString());
    }
}