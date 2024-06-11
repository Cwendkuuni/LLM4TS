import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper entityUnescaper;
    private StringBuffer buffer;
    private Writer writer;

    @Before
    public void setUp() throws Exception {
        entityUnescaper = new NumericEntityUnescaper();
        buffer = new StringBuffer();
        writer = new StringWriter(buffer);
    }

    @Test
    public void testTranslate() {
        try {
            assertEquals(7, entityUnescaper.translate("&#97;", 0, writer));
            assertEquals("a", buffer.toString());
            buffer.setLength(0);

            assertEquals(6, entityUnescaper.translate("&#x61;", 0, writer));
            assertEquals("a", buffer.toString());
            buffer.setLength(0);

            assertEquals(4, entityUnescaper.translate("&#38;", 0, writer));
            assertEquals("&", buffer.toString());
            buffer.setLength(0);

            assertEquals(4, entityUnescaper.translate("&#120;", 0, writer));
            assertEquals("x", buffer.toString());
            buffer.setLength(0);

            assertEquals(3, entityUnescaper.translate("&#65;", 0, writer));
            assertEquals("A", buffer.toString());
            buffer.setLength(0);
        } catch (IOException e) {
            fail("IOException was thrown.");
        }
    }

    @Test
    public void testTranslate_withInvalidInput() {
        try {
            assertEquals(0, entityUnescaper.translate("&#;", 0, writer));
            assertEquals("", buffer.toString());
            buffer.setLength(0);

            assertEquals(0, entityUnescaper.translate("&#", 0, writer));
            assertEquals("", buffer.toString());
            buffer.setLength(0);

            assertEquals(0, entityUnescaper.translate("&#x;", 0, writer));
            assertEquals("", buffer.toString());
            buffer.setLength(0);

            assertEquals(0, entityUnescaper.translate("&#x", 0, writer));
            assertEquals("", buffer.toString());
            buffer.setLength(0);

            assertEquals(0, entityUnescaper.translate("&#256;", 0, writer));
            assertEquals("", buffer.toString());
            buffer.setLength(0);
        } catch (IOException e) {
            fail("IOException was thrown.");
        }
    }
}