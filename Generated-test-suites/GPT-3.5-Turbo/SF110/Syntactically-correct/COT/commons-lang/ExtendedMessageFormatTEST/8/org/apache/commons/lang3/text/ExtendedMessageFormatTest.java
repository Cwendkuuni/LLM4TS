import static org.junit.Assert.*;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    @Test
    public void testExtendedMessageFormatWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US);
        assertEquals("Test {0}", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", registry);
        assertEquals("Test {0}", emf.toPattern());
        assertNotNull(emf.getFormat("test"));
    }

    @Test
    public void testExtendedMessageFormatWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US, registry);
        assertEquals("Test {0}", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
        assertNotNull(emf.getFormat("test"));
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New Test {0}");
        assertEquals("New Test {0}", emf.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0}");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0}");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    class TestFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return null;
        }
    }
}