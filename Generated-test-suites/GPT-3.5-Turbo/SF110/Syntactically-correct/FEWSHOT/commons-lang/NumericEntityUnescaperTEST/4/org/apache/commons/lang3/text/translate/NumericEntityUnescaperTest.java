import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testIsSet() {
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate_ValidInput() throws IOException {
        CharSequence input = "This is a test &#65; &#x41;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(12, result);
        assertEquals("This is a test A A", writer.toString());
    }

    @Test
    public void testTranslate_SemiColonRequiredOptionNotSet() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        CharSequence input = "This is a test &#65;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(10, result);
        assertEquals("This is a test A;", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColonOptionSet_NoSemiColon() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "This is a test &#65;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_ErrorIfNoSemiColonOptionSet_SemiColonPresent() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "This is a test &#65;;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(12, result);
        assertEquals("This is a test A;", writer.toString());
    }

    @Test
    public void testTranslate_InvalidNumberFormat() throws IOException {
        CharSequence input = "This is a test &#xInvalid;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EntityValueGreaterThan65535() throws IOException {
        CharSequence input = "This is a test &#65536;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(9, result);
        assertEquals("This is a test ��", writer.toString());
    }

    @Test
    public void testTranslate_InvalidInput() throws IOException {
        CharSequence input = "This is a test";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}