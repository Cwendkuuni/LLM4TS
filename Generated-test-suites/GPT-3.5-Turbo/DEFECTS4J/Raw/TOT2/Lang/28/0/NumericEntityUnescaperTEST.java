import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_decimalEntity() throws IOException {
        StringWriter writer = new StringWriter();
        numericEntityUnescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_hexEntity() throws IOException {
        StringWriter writer = new StringWriter();
        numericEntityUnescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_invalidEntity() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(0, numericEntityUnescaper.translate("&#", 0, writer));
    }

    @Test
    public void testTranslate_exceptionHandling() {
        try {
            StringWriter writer = new StringWriter();
            numericEntityUnescaper.translate("&#65;", 0, writer);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
    }
}