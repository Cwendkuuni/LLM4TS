import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidDecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder out = new StringBuilder();
        int index = 0;
        int result = unescaper.translate("&#65;", index, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_withValidHexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder out = new StringBuilder();
        int index = 0;
        int result = unescaper.translate("&#x41;", index, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder out = new StringBuilder();
        int index = 0;
        int result = unescaper.translate("&#invalid;", index, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder out = new StringBuilder();
        int index = 0;
        int result = unescaper.translate("", index, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withOutOfBoundsIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder out = new StringBuilder();
        int index = 5;
        int result = unescaper.translate("&#65;", index, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

}