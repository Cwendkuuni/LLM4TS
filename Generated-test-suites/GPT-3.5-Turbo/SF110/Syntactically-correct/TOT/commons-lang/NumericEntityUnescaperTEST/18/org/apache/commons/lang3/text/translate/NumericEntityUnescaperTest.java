import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper();
        CharSequence input = "ABC";
        int index = 0;
        Writer out = new StringWriter();

        int result = numericEntityUnescaper.translate(input, index, out);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_DecimalNumericEntity_ReturnsCorrectValue() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        int index = 0;
        Writer out = new StringWriter();

        int result = numericEntityUnescaper.translate(input, index, out);

        assertEquals(9, out.toString().length());
        assertEquals('A', out.toString().charAt(0));
        assertEquals('B', out.toString().charAt(1));
        assertEquals('C', out.toString().charAt(2));
        assertEquals('D', out.toString().charAt(3));
        assertEquals('E', out.toString().charAt(4));
        assertEquals('F', out.toString().charAt(5));
        assertEquals('G', out.toString().charAt(6));
        assertEquals('H', out.toString().charAt(7));
        assertEquals('I', out.toString().charAt(8));
    }

    @Test
    public void testTranslate_HexadecimalNumericEntity_ReturnsCorrectValue() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x41;";
        int index = 0;
        Writer out = new StringWriter();

        int result = numericEntityUnescaper.translate(input, index, out);

        assertEquals(3, out.toString().length());
        assertEquals('A', out.toString().charAt(0));
        assertEquals('B', out.toString().charAt(1));
        assertEquals('C', out.toString().charAt(2));
    }

    @Test
    public void testTranslate_HighValueDecimalNumericEntity_ReturnsCorrectValue() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65536;";
        int index = 0;
        Writer out = new StringWriter();

        int result = numericEntityUnescaper.translate(input, index, out);

        assertEquals(2, out.toString().length());
        assertEquals(0xD800, out.toString().charAt(0));
        assertEquals(0xDC00, out.toString().charAt(1));
    }

    @Test
    public void testTranslate_NoSemicolonRequiredOption_ReturnsCorrectValue() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65";
        int index = 0;
        Writer out = new StringWriter();

        int result = numericEntityUnescaper.translate(input, index, out);

        assertEquals(9, out.toString().length());
        assertEquals('A', out.toString().charAt(0));
        assertEquals('B', out.toString().charAt(1));
        assertEquals('C', out.toString().charAt(2));
        assertEquals('D', out.toString().charAt(3));
        assertEquals('E', out.toString().charAt(4));
        assertEquals('F', out.toString().charAt(5));
        assertEquals('G', out.toString().charAt(6));
        assertEquals('H', out.toString().charAt(7));
        assertEquals('I', out.toString().charAt(8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemicolonOption_ThrowsIllegalArgumentException() throws Exception {
        numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        int index = 0;
        Writer out = new StringWriter();

        numericEntityUnescaper.translate(input, index, out);
    }
}