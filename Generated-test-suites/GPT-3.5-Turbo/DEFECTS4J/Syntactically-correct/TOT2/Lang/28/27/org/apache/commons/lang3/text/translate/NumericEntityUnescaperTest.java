import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_ValidHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_InvalidEntityFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#", 0, writer);
        assertEquals(0, result);
        assertEquals(0, writer.toString().length());
    }

    @Test
    public void testTranslate_NonNumericEntityValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#notanumber;", 0, writer);
        assertEquals(0, result);
        assertEquals(0, writer.toString().length());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslate_OutOfBounds() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 5, writer);
    }
}