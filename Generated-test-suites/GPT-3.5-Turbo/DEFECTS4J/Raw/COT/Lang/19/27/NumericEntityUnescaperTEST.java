import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_basicHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_missingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65", 0, writer);

        assertEquals(5, result);
        assertEquals("&#65", writer.toString());
    }

    @Test
    public void testTranslate_invalidDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#123a;", 0, writer);

        assertEquals(7, result);
        assertEquals("&#123a;", writer.toString());
    }

    @Test
    public void testTranslate_invalidHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41a;", 0, writer);

        assertEquals(8, result);
        assertEquals("&#x41а;", writer.toString());
    }

    @Test
    public void testTranslate_highCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#128514;", 0, writer);

        assertEquals(11, result);
        assertEquals("\uD83D\uDE02", writer.toString());
    }
}


