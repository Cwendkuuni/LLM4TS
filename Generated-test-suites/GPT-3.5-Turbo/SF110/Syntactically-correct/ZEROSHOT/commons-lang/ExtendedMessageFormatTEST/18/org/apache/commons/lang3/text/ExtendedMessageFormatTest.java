import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import java.util.HashMap;
import java.text.Format;
import org.apache.commons.lang3.text.FormatFactory;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;

    @Before
    public void setUp() {
        format = new ExtendedMessageFormat("");
    }

    @Test
    public void testConstructor1() {
        format = new ExtendedMessageFormat("Hello");
        assertEquals("Hello", format.toPattern());
    }

    @Test
    public void testConstructor2() {
        format = new ExtendedMessageFormat("Hello", Locale.US);
        assertEquals("Hello", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        format = new ExtendedMessageFormat("Hello", registry);
        assertEquals("Hello", format.toPattern());
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        format = new ExtendedMessageFormat("Hello", Locale.US, registry);
        assertEquals("Hello", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testToPattern() {
        format = new ExtendedMessageFormat("Hello");
        assertEquals("Hello", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Goodbye {0}");
        assertEquals("Goodbye {0}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        format.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        format.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        format = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat other = new ExtendedMessageFormat("Hello");
        assertTrue(format.equals(other));
    }

    @Test
    public void testHashCode() {
        format = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat other = new ExtendedMessageFormat("Hello");
        assertEquals(format.hashCode(), other.hashCode());
    }

    @Test
    public void testGetFormat() {
        format = new ExtendedMessageFormat("Hello");
        assertNull(format.getFormat("invalid"));

        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        FormatFactory factory = new FormatFactory() {
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        };
        registry.put("custom", factory);
        format = new ExtendedMessageFormat("Hello", registry);
        assertNull(format.getFormat("invalid"));
        assertNotNull(format.getFormat("custom"));
    }

    @Test
    public void testReadArgumentIndex() {
        format.applyPattern("Hello {0}");
        assertEquals(0, format.readArgumentIndex(format.toPattern(), new ParsePosition(7)));
    }

    @Test
    public void testParseFormatDescription() {
        format.applyPattern("Hello {0,number,#.#}");
        assertEquals("number,#.#", format.parseFormatDescription(format.toPattern(), new ParsePosition(7)));
    }

    @Test
    public void testInsertFormats() {
        format.applyPattern("Hello {0,number,#.#}");
        ArrayList<String> customPatterns = new ArrayList<String>();
        customPatterns.add("custom");
        assertEquals("Hello {0,number,#.#,custom}", format.insertFormats(format.toPattern(), customPatterns));
    }

    @Test
    public void testSeekNonWs() {
        format.applyPattern("  Hello");
        ParsePosition pos = new ParsePosition(0);
        format.seekNonWs(format.toPattern(), pos);
        assertEquals(2, pos.getIndex());
    }

    @Test
    public void testNext() {
        ParsePosition pos = new ParsePosition(0);
        format.next(pos);
        assertEquals(1, pos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        StringBuilder sb = new StringBuilder();
        format.appendQuotedString("'Hello'", new ParsePosition(1), sb, true);
        assertEquals("'Hello'", sb.toString());
    }

    @Test
    public void testGetQuotedString() {
        format.getQuotedString("'Hello'", new ParsePosition(0), true);
    }

    @Test
    public void testContainsElements() {
        assertFalse(format.containsElements(null));

        Collection<String> coll1 = new ArrayList<String>();
        assertFalse(format.containsElements(coll1));

        Collection<String> coll2 = new ArrayList<String>();
        coll2.add(null);
        assertFalse(format.containsElements(coll2));

        Collection<String> coll3 = new ArrayList<String>();
        coll3.add("element");
        assertTrue(format.containsElements(coll3));
    }
}