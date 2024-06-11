import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_ValidDecimalEntity() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals(2, result);
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_ValidHexEntity() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#x41;", 0, writer);
            assertEquals(3, result);
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_InvalidEntity() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&invalid;", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_EmptyInput() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_InvalidHexEntity() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#xG;", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_ArrayIndexOutOfBounds() {
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#65;", 10, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}