import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_WhenInputIsNumericEntityWithSemicolon_ThenReturnsCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(9, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputIsNumericEntityWithoutSemicolon_ThenReturnsCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("&#65", 0, writer);

        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputIsHexNumericEntity_ThenReturnsCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(10, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputIsInvalidNumericEntity_ThenReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("&#invalid;", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputContainsMultipleNumericEntities_ThenReturnsCodepoints() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("&#65;&#66;&#67;", 0, writer);

        assertEquals(15, result);
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputContainsSpecialCharacter_ThenReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        int result = unescaper.translate("ABC&;", 3, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}