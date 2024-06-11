
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    }

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsDecodedCharacter() throws IOException {
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_ValidHexEntity_ReturnsDecodedCharacter() throws IOException {
        CharSequence input = "&#x41;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(6, result);
    }

    @Test
    public void testTranslate_ValidDecimalEntityWithSemicolon_ReturnsDecodedCharacter() throws IOException {
        CharSequence input = "&#65;;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(6, result);
    }

    @Test
    public void testTranslate_ValidHexEntityWithSemicolon_ReturnsDecodedCharacter() throws IOException {
        CharSequence input = "&#x41;;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(7, result);
    }

    @Test
    public void testTranslate_ValidDecimalEntityWithInvalidSemicolon_ReturnsDecodedCharacterAndConsumesExtraCharacter() throws IOException {
        CharSequence input = "&#65;;invalid";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertEquals("invalid", out.toString());
        assertEquals(6, result);
    }

    @Test
    public void testTranslate_ValidHexEntityWithInvalidSemicolon_ReturnsDecodedCharacterAndConsumesExtraCharacter() throws IOException {
        CharSequence input = "&#x41;;invalid";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertEquals("invalid", out.toString());
        assertEquals(7, result);
    }

    @Test
    public void testTranslate_ValidDecimalEntityWithInvalidOptionalSemicolon_ReturnsDecodedCharacter() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        CharSequence input = "&#65;;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(6, result);
    }

    @Test
    public void testTranslate_ValidHexEntityWithInvalidOptionalSemicolon_ReturnsDecodedCharacter() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        CharSequence input = "&#x41;;";
        StringWriter out = new StringWriter();

        int result = unescaper.translate(input, 0, out);

        assertTrue(out.toString().isEmpty());
        assertEquals(7, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ValidDecimalEntityWithInvalidErrorSemicolon_ThrowsIllegalArgumentException() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "&#65;;";
        StringWriter out = new StringWriter();

        unescaper.translate(input, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ValidHexEntityWithInvalidErrorSemicolon_ThrowsIllegalArgumentException() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "&#x41;;";
        StringWriter out = new StringWriter();

        unescaper.translate(input, 0, out);
    }

    @Test
    public void testIsSet_NullOption_ReturnsFalse() {
        assertFalse(unescaper.isSet(null));
    }

    @Test
    public void testIsSet_ExistingOption_ReturnsTrue() {
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
}
