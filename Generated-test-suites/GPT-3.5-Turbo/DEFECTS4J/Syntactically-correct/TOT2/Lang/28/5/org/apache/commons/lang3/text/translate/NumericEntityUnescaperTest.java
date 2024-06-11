import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDecimalEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithHexEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#invalid;", 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithIncompleteEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
        assertEquals(0, writer.toString().length());
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("", 0, writer);
        assertEquals(0, writer.toString().length());
    }

    @Test
    public void testTranslateWithInvalidInputIndex() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 1, writer);
        assertEquals(0, writer.toString().length());
    }
}