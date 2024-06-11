import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test lowercase language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));

        // Test invalid language
        assertNull(LocaleUtils.toLocale("ABC"));

        // Test empty country
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("en_"));

        // Test lowercase country
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));

        // Test invalid country
        assertNull(LocaleUtils.toLocale("en_XX"));

        // Test language and country
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));

        // Test language, country and variant
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_variant"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "CA", "variant");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);

        // Test empty list
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());

        // Test single locale
        List<Locale> singleLocaleList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(Collections.singletonList(Locale.ENGLISH), lookupList);

        // Test multiple locales
        List<Locale> multipleLocalesList = LocaleUtils.localeLookupList(Locale.FRENCH);
        List<Locale> expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH);
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "UK");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_variant");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(expectedList, langs);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedList = Arrays.asList(Locale.ENGLAND, Locale.AUSTRALIA, Locale.CANADA, Locale.IRLAND, Locale.NEW_ZEALAND, Locale.SOUTH_AFRICA, Locale.US);
        assertEquals(expectedList, countries);
    }
}