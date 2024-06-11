import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_PositiveDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(6, unescaper.translate("&#65;", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_NegativeDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(0, unescaper.translate("&#-65;", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_PositiveHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(7, unescaper.translate("&#x41;", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_NegativeHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(0, unescaper.translate("&#x-41;", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InvalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(0, unescaper.translate("&#invalid;", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InvalidEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(0, unescaper.translate("&#99999;", 0, new StringWriter()));
    }
    
}