import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Message");
        assertEquals("Message", emf1.toPattern());

        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", emf2.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello, {0}!");
        assertEquals("Hello, {0}!", emf.toPattern());

        emf.applyPattern("Hi, {0}, {1}!");
        assertEquals("Hi, {0}, {1}!", emf.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        assertTrue(emf1.equals(emf2));

        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Hi, {0}!");
        assertFalse(emf3.equals(emf4));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(emf1.hashCode(), emf2.hashCode());

        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Hi, {0}!");
        assertNotEquals(emf3.hashCode(), emf4.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.getFormat("unknown"));

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        registry.put("date", new SimpleDateFormatFactory());
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("", registry);
        assertNotNull(emf2.getFormat("number"));
        assertNotNull(emf2.getFormat("date"));
        assertNull(emf2.getFormat("unknown"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormats(new Format[]{new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
    }

    private static class NumberFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return NumberFormat.getInstance(locale);
        }
    }

    private static class SimpleDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return new SimpleDateFormat(args, locale);
        }
    }
}