import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.FormatFactoryImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setup() {
        String pattern = "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.";
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        formatFactory = new FormatFactoryImpl();
        registry.put("Format1", formatFactory);

        messageFormat = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testConstructorWithPattern() {
        assertNotNull(messageFormat);
        assertEquals(messageFormat.getClass(), ExtendedMessageFormat.class);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        Locale locale = Locale.US;
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(
                "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.",
                locale, registry);
        assertNotNull(messageFormat);
        assertEquals(messageFormat.getLocale(), locale);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(
                "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.",
                registry);
        assertNotNull(messageFormat);
        assertEquals(messageFormat.getRegistry().size(), 1);
    }

    @Test
    public void testConstructorWithPatternRegistryAndLocale() {
        Locale locale = Locale.US;
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(
                "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.",
                registry, locale);
        assertNotNull(messageFormat);
        assertEquals(messageFormat.getLocale(), locale);
    }

    @Test
    public void testToPattern() {
        assertEquals(messageFormat.toPattern(), "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.");
    }

    @Test
    public void testApplyPattern() {
        String[] args = {"one", "two"};
        Object[] objects = messageFormat.apply(args);
        assertEquals(objects[0].toString(), "Hello oneFormat1");
        assertEquals(objects[1].toString(), "Hello twoFormat2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        String pattern = "Hello {0, choice, 0#Default, 1#Custom}{1, number, 0#Format1, 1#Format2}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"one", "two"};
        messageFormat.apply(args);

        // The following line should throw an IllegalArgumentException
        messageFormat.applyPattern("Invalid pattern");
    }

    @Test
    public void testGetFormat() {
        assertEquals(messageFormat.getFormat("Format1"), formatFactory.getFormat("Format1", null, null));
        assertNull(messageFormat.getFormat("InvalidFormat"));
    }

    @Test
    public void testSetFormat() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        throw new UnsupportedOperationException();
    }
}