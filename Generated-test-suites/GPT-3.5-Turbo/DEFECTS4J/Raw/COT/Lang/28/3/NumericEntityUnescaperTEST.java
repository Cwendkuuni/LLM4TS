import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_BasicScenario() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals("A", writer.toString());
        assertEquals(6, result);
    }

    @Test
    public void testTranslate_NoNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("Hello world", 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_InvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#XYZ;", 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_NumericEntityWithLowercaseX() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals("A", writer.toString());
        assertEquals(7, result);
    }
}