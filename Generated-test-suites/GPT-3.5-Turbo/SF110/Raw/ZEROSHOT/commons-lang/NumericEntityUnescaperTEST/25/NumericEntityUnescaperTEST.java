
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidNumericEntity_ReturnsTranslatedValue() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        int index = 0;
        StringWriter out = new StringWriter();

        // Act
        int result = unescaper.translate(input, index, out);

        // Assert
        assertEquals(4, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity_ReturnsZero() throws IOException {
        // Arrange
        CharSequence input = "&#invalid;";
        int index = 0;
        StringWriter out = new StringWriter();

        // Act
        int result = unescaper.translate(input, index, out);

        // Assert
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_SemiColonRequiredOptionSet_ReturnsZeroIfSemiColonMissing() throws IOException {
        // Arrange
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65";
        int index = 0;
        StringWriter out = new StringWriter();

        // Act
        int result = unescaper.translate(input, index, out);

        // Assert
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColonOptionSet_ThrowsExceptionIfSemiColonMissing() throws IOException {
        // Arrange
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        int index = 0;
        StringWriter out = new StringWriter();

        // Act
        unescaper.translate(input, index, out);
    }
}

