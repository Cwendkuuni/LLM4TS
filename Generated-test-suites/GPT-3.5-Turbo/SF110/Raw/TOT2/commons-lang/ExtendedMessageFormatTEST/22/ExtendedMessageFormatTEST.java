import static org.junit.Assert.*;
import org.junit.Test;
import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
    }

    @Test
    public void testToPattern() {
        String expected = "Test {0}";
        assertEquals(expected, extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        extendedMessageFormat.applyPattern("New pattern: {0}");
        String expected = "New pattern: {0}";
        assertEquals(expected, extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = {new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("#.00")};
        extendedMessageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = {new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("#.00")};
        extendedMessageFormat.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat other = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertTrue(extendedMessageFormat.equals(extendedMessageFormat));
        assertTrue(extendedMessageFormat.equals(other));
        assertFalse(extendedMessageFormat.equals(null));
        assertFalse(extendedMessageFormat.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat other = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertEquals(extendedMessageFormat.hashCode(), other.hashCode());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat extendedMessageFormatWithRegistry = new ExtendedMessageFormat("Date: {0,date}", Locale.getDefault(), registry);
        assertNotNull(extendedMessageFormatWithRegistry.getFormat("date"));
        assertNull(extendedMessageFormatWithRegistry.getFormat("number"));
    }

    @Test
    public void testReadArgumentIndex() {
        int argumentIndex = extendedMessageFormat.readArgumentIndex("Test {0}", new ParsePosition(6));
        assertEquals(0, argumentIndex);
    }

    @Test
    public void testParseFormatDescription() {
        String formatDescription = extendedMessageFormat.parseFormatDescription("Date: {0,date}", new ParsePosition(6));
        assertEquals("date", formatDescription);
    }

    @Test
    public void testInsertFormats() {
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date");
        String pattern = extendedMessageFormat.insertFormats("Date: {0,date}", customPatterns);
        assertEquals("Date: {0,date}", pattern);
    }

    @Test
    public void testSeekNonWs() {
        ParsePosition parsePosition = new ParsePosition(0);
        extendedMessageFormat.seekNonWs("  Test", parsePosition);
        assertEquals(2, parsePosition.getIndex());
    }

    @Test
    public void testNext() {
        ParsePosition parsePosition = new ParsePosition(0);
        ParsePosition nextPosition = extendedMessageFormat.next(parsePosition);
        assertEquals(1, nextPosition.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        StringBuilder result = new StringBuilder();
        ParsePosition parsePosition = new ParsePosition(0);
        extendedMessageFormat.appendQuotedString("Test 'quoted'", parsePosition, result, true);
        assertNotNull(result);
        assertEquals("quoted", result.toString());
    }

}