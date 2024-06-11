import org.junit.Test;

import java.text.Format;
import java.util.*;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        assertEquals("Hello John, Smith!", emf.format(new Object[]{"John", "Smith"}));
        assertEquals("Hello Alice, Doe!", emf.format(new Object[]{"Alice", "Doe"}));
        assertEquals("Hello David, Brown!", emf.format(new Object[]{"David", "Brown"}));
    }

    @Test
    public void testExtendedMessageFormatWithLocale() {
        Locale locale = Locale.GERMAN;
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Guten Tag {0}, {1}!", locale);

        assertEquals("Guten Tag John, Smith!", emf.format(new Object[]{"John", "Smith"}));
        assertEquals("Guten Tag Alice, Doe!", emf.format(new Object[]{"Alice", "Doe"}));
        assertEquals("Guten Tag David, Brown!", emf.format(new Object[]{"David", "Brown"}));
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,number}, {1,date}!", registry);

        assertEquals("Hello 123, 2021-10-01!", emf.format(new Object[]{123, new Date(121, 9, 1)}));
        assertEquals("Hello 456, 2022-05-01!", emf.format(new Object[]{456, new Date(122, 4, 1)}));
        assertEquals("Hello 789, 2023-12-01!", emf.format(new Object[]{789, new Date(123, 11, 1)}));
    }

    @Test
    public void testExtendedMessageFormatWithLocaleAndRegistry() {
        Locale locale = Locale.GERMAN;
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Guten Tag {0,number}, {1,date}!", locale, registry);

        assertEquals("Guten Tag 123, 01.10.2021!", emf.format(new Object[]{123, new Date(121, 9, 1)}));
        assertEquals("Guten Tag 456, 01.05.2022!", emf.format(new Object[]{456, new Date(122, 4, 1)}));
        assertEquals("Guten Tag 789, 01.12.2023!", emf.format(new Object[]{789, new Date(123, 11, 1)}));
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        assertEquals("Hello {0}, {1}!", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1,date}!");

        emf.applyPattern("Goodbye {0}, {1,date}!");

        assertEquals("Goodbye {0}, {1,date}!", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        emf.setFormatByArgumentIndex(1, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        emf.setFormats(new Format[2]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}!");

        emf.setFormatsByArgumentIndex(new Format[2]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}, {1,date}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}, {1,date}!");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Goodbye {0}, {1,date}!");

        assertTrue(emf1.equals(emf1));
        assertTrue(emf1.equals(emf2));
        assertFalse(emf1.equals(emf3));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}, {1,date}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}, {1,date}!");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Goodbye {0}, {1,date}!");

        assertTrue(emf1.hashCode() == emf1.hashCode());
        assertTrue(emf1.hashCode() == emf2.hashCode());
        assertFalse(emf1.hashCode() == emf3.hashCode());
    }

    private static class NumberFormatFactory implements FormatFactory {
        public Format getFormat(String name, String arguments, Locale locale) {
            return NumberFormat.getInstance(locale);
        }
    }

    private static class DateFormatFactory implements FormatFactory {
        public Format getFormat(String name, String arguments, Locale locale) {
            return DateFormat.getDateInstance(Integer.parseInt(arguments), locale);
        }
    }
}