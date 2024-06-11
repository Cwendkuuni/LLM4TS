
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(Locale.CANADA, LocaleUtils.toLocale("en_CA"));
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");
        assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2),
                LocaleUtils.localeLookupList(locale1, locale2));
        assertEquals(Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), locale2),
                LocaleUtils.localeLookupList(new Locale("fr", "CA"), locale2));
        assertEquals(Collections.singletonList(locale2),
                LocaleUtils.localeLookupList(locale2, locale2));
        assertEquals(Collections.singletonList(null),
                LocaleUtils.localeLookupList(null, locale2));
    }

    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.CANADA));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.CANADA));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.singletonList(Locale.FRANCE),
                LocaleUtils.languagesByCountry("FR"));
        assertEquals(Collections.singletonList(Locale.ENGLISH),
                LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.singletonList(null),
                LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.singletonList(Locale.CANADA),
                LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.singletonList(Locale.GERMANY),
                LocaleUtils.countriesByLanguage("de"));
        assertEquals(Collections.singletonList(null),
                LocaleUtils.countriesByLanguage(null));
    }
}

