import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_normalDecimalValue() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_normalHexValue() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_noNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("test", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_invalidNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_invalidHexValue() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#xG;", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_invalidNumberFormat() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x;", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}