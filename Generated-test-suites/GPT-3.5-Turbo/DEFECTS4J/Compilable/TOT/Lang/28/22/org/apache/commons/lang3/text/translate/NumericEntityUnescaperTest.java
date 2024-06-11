package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsCorrectCodepoint() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#65;", 0, output);
        assertEquals("Incorrect number of characters translated", 6, charsTranslated);
        assertEquals("Incorrect output", "A", output.toString());
    }

    @Test
    public void testTranslate_ValidHexEntity_ReturnsCorrectCodepoint() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#x41;", 0, output);
        assertEquals("Incorrect number of characters translated", 7, charsTranslated);
        assertEquals("Incorrect output", "A", output.toString());
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#invalid;", 0, output);
        assertEquals("Incorrect number of characters translated", 0, charsTranslated);
        assertEquals("Output should be empty", "", output.toString());
    }

    @Test
    public void testTranslate_ValidDecimalEntityWithExtraCharacters_ReturnsCorrectCodepoint() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#65;extra;", 0, output);
        assertEquals("Incorrect number of characters translated", 6, charsTranslated);
        assertEquals("Incorrect output", "A", output.toString());
    }

    @Test
    public void testTranslate_ValidHexEntityWithExtraCharacters_ReturnsCorrectCodepoint() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#x41;extra;", 0, output);
        assertEquals("Incorrect number of characters translated", 7, charsTranslated);
        assertEquals("Incorrect output", "A", output.toString());
    }

    @Test
    public void testTranslate_InvalidEntityWithExtraCharacters_ReturnsZero() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#invalid;extra;", 0, output);
        assertEquals("Incorrect number of characters translated", 0, charsTranslated);
        assertEquals("Output should be empty", "", output.toString());
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsZero() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("", 0, output);
        assertEquals("Incorrect number of characters translated", 0, charsTranslated);
        assertEquals("Output should be empty", "", output.toString());
    }

    @Test
    public void testTranslate_InvalidIndex_ReturnsZero() throws IOException {
        StringWriter output = new StringWriter();
        int charsTranslated = numericEntityUnescaper.translate("&#65;", 5, output);
        assertEquals("Incorrect number of characters translated", 0, charsTranslated);
        assertEquals("Output should be empty", "", output.toString());
    }
}