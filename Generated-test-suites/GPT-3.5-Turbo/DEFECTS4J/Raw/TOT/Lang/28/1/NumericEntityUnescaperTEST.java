
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsCodepoint() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(2, result);
        Assert.assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_ValidHexEntity_ReturnsCodepoint() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#x61;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(3, result);
        Assert.assertEquals('a', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#invalid;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NoEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "abc";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalFormat_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#xyz;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexFormat_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#xzz;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalOverflow_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#1000000000000000;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexOverflow_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "&#x10000000000000000;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }
}

