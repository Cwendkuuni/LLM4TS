import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_ValidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(6, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_NoEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("abc", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#invalid;", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LargeDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#100000;", 0, writer);

        assertEquals(11, result);
        assertEquals('\uD869', writer.toString().charAt(0));
        assertEquals('\uDEB0', writer.toString().charAt(1));
    }

    @Test
    public void testTranslate_InvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#xinvalid;", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}