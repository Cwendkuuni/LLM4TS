import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translate method with decimal entities.
     */
    @Test
    public void testDecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;&#124;&#125;";
        String expected = "abc";
        String output = CharSequenceUtils.toString(unescaper.translate(new StringCharSequence(input), 0, new StringBuilder()));
        assertEquals(expected, output);
    }

    /**
     * Test translate method with hexadecimal entities.
     */
    @Test
    public void testHexadecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x61;&#x62;&#x63;";
        String expected = "abc";
        String output = CharSequenceUtils.toString(unescaper.translate(new StringCharSequence(input), 0, new StringBuilder()));
        assertEquals(expected, output);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        String output = CharSequenceUtils.toString(unescaper.translate(new StringCharSequence(input), 0, new StringBuilder()));
        assertEquals(input, output);
    }

    /**
     * Test translate method with no entity in input.
     */
    @Test
    public void testNoEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "abc";
        String output = CharSequenceUtils.toString(unescaper.translate(new StringCharSequence(input), 0, new StringBuilder()));
        assertEquals(input, output);
    }

    /**
     * Test translate method with invalid entity format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntityFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x invalid";
        unescaper.translate(new StringCharSequence(input), 0, new StringBuilder());
    }

    /**
     * Test translate method with invalid character at the beginning.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterAtStart() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "%&amp;#123;";
        unescaper.translate(new StringCharSequence(input), 0, new StringBuilder());
    }

    /**
     * Test translate method with invalid character at the end.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterAtEnd() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;x";
        unescaper.translate(new StringCharSequence(input), 0, new StringBuilder());
    }

    /**
     * Test translate method with unmatched number of characters in entity.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnmatchedNumberOfCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;x";
        unescaper.translate(new StringCharSequence(input), 0, new StringBuilder());
    }

    /**
     * Test translate method with entities containing control characters.
     */
    @Test
    public void testEntitiesWithControlCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#127;&#128;&#129;";
        String expected = "\u007f\u007f\u007f";
        String output = CharSequenceUtils.toString(unescaper.translate(new StringCharSequence(input), 0, new StringBuilder()));
        assertEquals(expected, output);
    }
}