import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals(5, result);
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_hexCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#x5A;", 0, writer);
            assertEquals(6, result);
            assertEquals("Z", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_missingSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_invalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#$;", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_highEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65536;", 0, writer);
            assertEquals(7, result);
            assertEquals("𐀀", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}