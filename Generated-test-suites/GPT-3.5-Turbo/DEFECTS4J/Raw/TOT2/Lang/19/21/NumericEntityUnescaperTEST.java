import org.junit.Test;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&invalid;", 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithLargeDecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#128514;", 0, writer);
        assertEquals("😂", writer.toString());
    }

    @Test
    public void testTranslateWithNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
        assertEquals("A", writer.toString());
    }
}