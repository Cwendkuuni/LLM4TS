import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setup() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}");
    }

    @Test
    public void testToPattern() {
        assertEquals("Test {0}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternNoRegistry() {
        extendedMessageFormat.applyPattern("New Test {0}");
        assertEquals("New Test {0}", extendedMessageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidFormat() {
        extendedMessageFormat.applyPattern("Invalid Test {");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat other = new ExtendedMessageFormat("Test {0}");
        assertTrue(extendedMessageFormat.equals(other));
    }

    @Test
    public void testNotEquals() {
        ExtendedMessageFormat other = new ExtendedMessageFormat("Other {0}");
        assertFalse(extendedMessageFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        assertEquals(extendedMessageFormat.hashCode(), extendedMessageFormat.hashCode());
    }

    @Test
    public void testGetFormatNoRegistry() {
        assertNull(extendedMessageFormat.getFormat("test"));
    }

    @Test
    public void testGetFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new DummyFormatFactory());
        ExtendedMessageFormat customFormat = new ExtendedMessageFormat("Custom Test {0}", registry);
        assertNotNull(customFormat.getFormat("test"));
    }

    private class DummyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return null;
        }
    }

    @Test
    public void testReadArgumentIndex() {
        ParsePosition pos = new ParsePosition(0);
        assertEquals(0, extendedMessageFormat.readArgumentIndex("Test {0}", pos));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexInvalid() {
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.readArgumentIndex("Invalid {", pos);
    }

    @Test
    public void testParseFormatDescription() {
        ParsePosition pos = new ParsePosition(0);
        assertEquals("0", extendedMessageFormat.parseFormatDescription("{0}", pos));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionInvalid() {
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.parseFormatDescription("{", pos);
    }

    @Test
    public void testInsertFormats() {
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("custom");
        assertEquals("Test {0,custom}", extendedMessageFormat.insertFormats("Test {0}", customPatterns));
    }

}