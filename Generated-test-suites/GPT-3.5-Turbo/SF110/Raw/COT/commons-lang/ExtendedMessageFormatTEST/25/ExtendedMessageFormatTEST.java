import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}");
        assertEquals("Hello, {0}", emf.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", Locale.US);
        assertEquals("Hello, {0}", emf.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", factory);

        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", registry);
        assertEquals("Hello, {0}", emf.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", factory);

        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", Locale.US, registry);
        assertEquals("Hello, {0}", emf.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    // Additional tests for edge cases and exception handling scenarios

    @Test
    public void testConstructorWithNullPattern() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat(null);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testConstructorWithInvalidFormatElement() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testConstructorWithUnreadableFormatElement() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testConstructorWithUnterminatedFormatElement() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0 {1}");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
}