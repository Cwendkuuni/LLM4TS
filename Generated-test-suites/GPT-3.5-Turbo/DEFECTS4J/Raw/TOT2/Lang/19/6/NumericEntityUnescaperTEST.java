import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithoutSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#invalid;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateHighValueEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#99999;", 0, writer);
        assertEquals(8, result);
        assertEquals("\uD85F\uDFC7", writer.toString());
    }

}