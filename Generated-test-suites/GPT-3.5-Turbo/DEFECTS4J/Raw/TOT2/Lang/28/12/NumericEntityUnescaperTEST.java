import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalDecimalEntity_Success() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NormalHexEntity_Success() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_MissingSemicolon_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_InvalidNumberFormat_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#ABC;", 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("", 0, writer);
        assertEquals(0, result);
    }
}