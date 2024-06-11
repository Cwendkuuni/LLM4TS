import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorPatternLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        assertNotNull(emf);
        assertEquals(emf.toPattern(), "Hello, {0}!");
    }

    @Test
    public void testConstructorPatternLocaleRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("You owe {0, currency} to {1}!", Locale.US, registry);
        assertNotNull(emf);
        assertEquals(emf.toPattern(), "You owe {0, currency} to {1}!");
    }

    @Test
    public void testConstructorPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test.");
        assertNotNull(emf);
        assertEquals(emf.toPattern(), "This is a test.");
    }

    @Test
    public void testConstructorPatternRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("The date today is {0, date}", registry);
        assertNotNull(emf);
        assertEquals(emf.toPattern(), "The date today is {0, date}");
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test.");
        assertEquals(emf.toPattern(), "This is a test.");
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("This is a test: {0}, {1}");
        assertEquals(emf.toPattern(), "This is a test: {0}, {1}");
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        assertTrue(emf1.equals(emf2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", registry);
        Format format = emf.getFormat("currency");
        assertNotNull(format);
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        int index = emf.readArgumentIndex("This is a test: {0}, {1}", pos);
        assertEquals(index, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexInvalidIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        int index = emf.readArgumentIndex("This is a test: {a}, {1}", pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexUnterminatedElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        int index = emf.readArgumentIndex("This is a test: {0, date", pos);
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String description = emf.parseFormatDescription("This is a test: {0, currency}", pos);
        assertEquals(description, "currency");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionUnterminatedElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String description = emf.parseFormatDescription("This is a test: {0, currency", pos);
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("currency");
        customPatterns.add(null);
        String pattern = emf.insertFormats("This is a test: {0}, {1}", customPatterns);
        assertEquals(pattern, "This is a test: {0, currency}, {1}");
    }

    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        StringBuilder result = emf.appendQuotedString("This is a test: '{0}, {1}'", pos, appendTo, true);
        assertEquals(result.toString(), "This is a test: {0}, {1}");
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs("   This is a test   ", pos);
        assertEquals(pos.getIndex(), 3);
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        ParsePosition result = emf.next(pos);
        assertEquals(result.getIndex(), 1);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> coll = new ArrayList<>();
        assertFalse(emf.containsElements(coll));
    }

}