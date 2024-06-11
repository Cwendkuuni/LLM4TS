import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalCase() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("This is a normal text.", 0, writer);
        assertEquals(0, translatedLength);
        assertEquals("This is a normal text.", writer.toString());
    }

    @Test
    public void testTranslate_IntegerInput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("&#65;", 0, writer);
        assertEquals(6, translatedLength);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_HexadecimalInput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("&#x41;", 0, writer);
        assertEquals(7, translatedLength);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NoAmpersand() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("#65;", 0, writer);
        assertEquals(0, translatedLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("&#invalid;", 0, writer);
        assertEquals(0, translatedLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EmptyInput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedLength = translator.translate("", 0, writer);
        assertEquals(0, translatedLength);
        assertEquals("", writer.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslate_OutOfBounds() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        translator.translate("&#65;", 10, writer);
    }

}