import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);

        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);

        result = unescaper.translate("&#500;", 0, writer);
        assertEquals(6, result);
        assertEquals("Ȕ", writer.toString());

        writer.getBuffer().setLength(0);

        result = unescaper.translate("&#x1F4A9;", 0, writer);
        assertEquals(10, result);
        assertEquals("💩", writer.toString());

        writer.getBuffer().setLength(0);

        result = unescaper.translate("&#9999999;", 0, writer);
        assertEquals(11, result);
        assertEquals("�����", writer.toString());
    }
}