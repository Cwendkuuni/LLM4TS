
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper neUnescaper;

    @Before
    public void setUp() {
        neUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidNumericEntity_ReturnsCorrectTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&amp;&#65;&#x66;&#xFF;";

        int result = neUnescaper.translate(input, 0, writer);

        assertEquals("Output is incorrect", 14, result);
        assertEquals("Decoded entity is incorrect", "&Aafÿ", writer.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&#";
        int index = 0;

        int result = neUnescaper.translate(input, index, writer);

        assertEquals("Output is incorrect", 0, result);
        assertEquals("Writer should remain empty for invalid entity", "", writer.toString());
    }

    @Test
    public void testTranslate_SemiColonNotRequiredButNoSemiColonExists_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        int result = neUnescaper.translate(input, index, writer);

        assertEquals("Output is incorrect", 0, result);
        assertEquals("Writer should remain empty for invalid entity", "", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_SemiColonRequiredButNoSemiColonExists_ThrowsIllegalArgumentException() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        neUnescaper.translate(input, index, writer);
    }

    @Test
    public void testTranslate_ValidHexEntityGreaterThanFFFF_ReturnsCorrectTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x0100";

        int result = neUnescaper.translate(input, 0, writer);

        assertEquals("Output is incorrect", 1, result);
        assertEquals("Decoded entity is incorrect", "\u0100", writer.toString());
    }

    @Test
    public void testTranslate_ValidDecimalEntityLessThanFFFF_ReturnsCorrectTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&#9999";

        int result = neUnescaper.translate(input, 0, writer);

        assertEquals("Output is incorrect", 4, result);
        assertEquals("Decoded entity is incorrect", "ϧ", writer.toString());
    }
}

