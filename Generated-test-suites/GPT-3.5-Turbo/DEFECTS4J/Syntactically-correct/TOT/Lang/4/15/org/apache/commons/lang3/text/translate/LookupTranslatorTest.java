import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator(
                new CharSequence[]{"a", "1"},
                new CharSequence[]{"b", "2"},
                new CharSequence[]{"c", "3"}
        );
    }

    @After
    public void tearDown() {
        lookupTranslator = null;
    }

    @Test
    public void testTranslate_typicalCase() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(1, lookupTranslator.translate("abc", 0, writer));
        assertEquals("1", writer.toString());
    }

    @Test
    public void testTranslate_edgeCase() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(1, lookupTranslator.translate("a", 0, writer));
        assertEquals("1", writer.toString());
    }

    @Test
    public void testTranslate_errorCase() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(0, lookupTranslator.translate("xyz", 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_multiCharSequence() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(2, lookupTranslator.translate("bb", 0, writer));
        assertEquals("22", writer.toString());
    }
}