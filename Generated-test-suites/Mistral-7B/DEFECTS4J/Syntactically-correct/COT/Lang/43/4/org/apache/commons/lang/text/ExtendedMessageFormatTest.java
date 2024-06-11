import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructors() {
        String pattern = "Hello {0, choice, 0#Mr|1#Mrs|1#Ms}{1}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());

        String patternLocale = "Hello {0, choice, 0#Mr|1#Mrs|1#Ms}{1}!";
        Locale locale = new Locale("en", "US");
        ExtendedMessageFormat formatLocale = new ExtendedMessageFormat(pattern, locale);
        assertEquals("", formatLocale.toPattern());

        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory mockFormatFactory = mock(FormatFactory.class);
        registry.put("MyFormat", mockFormatFactory);
        String patternRegistry = "{0, format, MyFormat}";
        ExtendedMessageFormat formatRegistry = new ExtendedMessageFormat(patternRegistry, registry);
        assertEquals("{0, format, MyFormat}", formatRegistry.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        String pattern = "Hello {0, choice, 0#Mr|1#Mrs|1#Ms}{1}!{";
        new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testGetFormat() {
        String formatName = "StringFormat";
        FormatFactory mockFormatFactory = mock(FormatFactory.class);
        when(mockFormatFactory.getFormat(formatName, "", Locale.getDefault()))
                .thenReturn(new StringFormat("%s"));
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(formatName, mockFormatFactory);
        String pattern = "{0, format, " + formatName + "}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Format formatted = format.getFormat(0);
        assertEquals(StringFormat.class, formatted.getClass());
        assertEquals("%s", formatted.pattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormat() {
        String formatName = "MissingFormat";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format, "
                + formatName + "}", null, registry);
        format.applyPattern("");
        fail("An IllegalArgumentException should have been thrown.");
    }

    @Test
    public void testApplyPattern() {
        String formatName = "StringFormat";
        FormatFactory mockFormatFactory = mock(FormatFactory.class);
        when(mockFormatFactory.getFormat(formatName, "", Locale.getDefault()))
                .thenReturn(new StringFormat("%s"));
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put(formatName, mockFormatFactory);
        String pattern = "{0, format, " + formatName + "}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        String[] args = {"Test"};
        Object[] objects = format.format(args);
        assertEquals(1, objects.length);
        assertEquals("Test", objects[0]);
    }
}