import org.junit.Test;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
        assertEquals(Locale.getDefault(), emf.getLocale());
    }

    @Test
    public void testConstructorCustomLocale() {
        Locale customLocale = new Locale("en", "US");
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", customLocale);
        assertNotNull(emf);
        assertEquals(customLocale, emf.getLocale());
    }

    @Test
    public void testConstructorDefaultRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
        assertNull(emf.registry);
    }

    @Test
    public void testConstructorCustomRegistry() {
        Map<String, FormatFactory> customRegistry = new HashMap<>();
        customRegistry.put("custom", new DummyFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", customRegistry);
        assertNotNull(emf);
        assertEquals(customRegistry, emf.registry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testApplyPatternDefaultRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("NewTest");
        assertEquals("NewTest", emf.toPattern());
    }

    @Test
    public void testApplyPatternCustomRegistry() {
        Map<String, FormatFactory> customRegistry = new HashMap<>();
        customRegistry.put("custom", new DummyFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", customRegistry);
        emf.applyPattern("New{1,custom}");
        assertEquals("New{1,custom}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEqualsAndHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Other");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Test", new DummyFormatFactory());

        assertTrue(emf1.equals(emf1));
        assertTrue(emf1.equals(emf2));
        assertFalse(emf1.equals(null));
        assertFalse(emf1.equals(new Object()));
        assertFalse(emf1.equals(emf3));
        assertFalse(emf1.equals(emf4));

        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("'{0}'");
        assertEquals("'{0}'", emf.insertFormats("'{0}'", null));

        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add(null);
        customPatterns.add("custom");

        assertEquals("'{0}',custom", emf.insertFormats("'{0}'", customPatterns));
    }

    private class DummyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return null;
        }
    }
}