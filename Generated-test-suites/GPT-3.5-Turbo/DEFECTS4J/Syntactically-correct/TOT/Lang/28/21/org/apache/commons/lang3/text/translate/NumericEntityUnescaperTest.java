import org.junit.Test;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withValidNumericEntity_shouldReturnCodepoint() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#65;", 0, writer);
        assertEquals(2, translationLength);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withHexNumericEntity_shouldReturnCodepoint() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#x41;", 0, writer);
        assertEquals(4, translationLength);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldReturnZero() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#invalid;", 0, writer);
        assertEquals(0, translationLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnZero() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("", 0, writer);
        assertEquals(0, translationLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexNumericEntity_shouldReturnZero() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#xINVALID;", 0, writer);
        assertEquals(0, translationLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withMissingSemicolon_shouldReturnZero() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#65", 0, writer);
        assertEquals(0, translationLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexCharacter_shouldReturnZero() throws Exception {
        StringWriter writer = new StringWriter();
        int translationLength = unescaper.translate("&#xGG;", 0, writer);
        assertEquals(0, translationLength);
        assertEquals("", writer.toString());
    }

}