import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexadecimalEntity_returnsCorrectCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int index = unescaper.translate("&#x41;", 0, writer);

            assertEquals(6, index);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withValidDecimalEntity_returnsCorrectCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int index = unescaper.translate("&#65;", 0, writer);

            assertEquals(6, index);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withInvalidEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int index = unescaper.translate("&#;", 0, writer);

            assertEquals(0, index);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withInvalidHexadecimalEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int index = unescaper.translate("&#xG;", 0, writer);

            assertEquals(0, index);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withInvalidInput_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int index = unescaper.translate("123", 0, writer);

            assertEquals(0, index);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}