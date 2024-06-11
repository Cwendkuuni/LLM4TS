import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private final NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withProperNumericEntity_ReturnsCodePoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65;", 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withHexNumericEntity_ReturnsCodePoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#x41;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#xG;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withArrayIndexOutOfBounds_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65;", 5, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNonNumericEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("ABC", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withLargeNumericEntity_ReturnsCodePoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#123456;", 0, writer);
        assertEquals(8, result);
        assertEquals("\uD83D\uDE00", writer.toString());
    }

}