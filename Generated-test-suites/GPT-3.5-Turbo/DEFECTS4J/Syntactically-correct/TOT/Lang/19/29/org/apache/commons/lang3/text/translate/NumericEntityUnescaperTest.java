import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result, "Incorrect result");
        assertEquals('A', writer.toString().charAt(0), "Incorrect translation");
    }

    @Test
    public void testTranslate_validHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(6, result, "Incorrect result");
        assertEquals('A', writer.toString().charAt(0), "Incorrect translation");
    }

    @Test
    public void testTranslate_noSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(4, result, "Incorrect result");
        assertEquals('A', writer.toString().charAt(0), "Incorrect translation");
    }

    @Test
    public void testTranslate_invalidFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#A;", 0, writer);
        assertEquals(0, result, "Incorrect result");
        assertEquals("", writer.toString(), "Incorrect translation");
    }

    @Test
    public void testTranslate_invalidNumericValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#100000;", 0, writer);
        assertEquals(10, result, "Incorrect result");
        assertEquals("", writer.toString(), "Incorrect translation");
    }

    @Test
    public void testTranslate_highCodepointValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, writer);
        assertEquals(11, result, "Incorrect result");
        assertEquals("\uD800\uDC00", writer.toString(), "Incorrect translation");
    }

}