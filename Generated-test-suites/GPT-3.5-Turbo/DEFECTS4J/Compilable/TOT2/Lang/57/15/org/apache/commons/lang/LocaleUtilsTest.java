package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        // Typical use case
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_edgeCases() {
        // Edge cases
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_errorScenario() {
        // Error scenario
        LocaleUtils.toLocale("xyz"); // Invalid locale format
    }

    @Test
    public void testLocaleLookupList_basic() {
        // Typical use case
        assertEquals(4, LocaleUtils.localeLookupList(Locale.US).size());
    }

    @Test
    public void testAvailableLocaleList_basic() {
        // Typical use case
        assertEquals(LocaleUtils.availableLocaleList().size(), Locale.getAvailableLocales().length);
    }

    @Test
    public void testAvailableLocaleSet_basic() {
        // Typical use case
        assertEquals(LocaleUtils.availableLocaleSet().size(), LocaleUtils.availableLocaleList().size());
    }

    @Test
    public void testIsAvailableLocale_basic() {
        // Typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry_basic() {
        // Typical use case
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
    }

    @Test
    public void testCountriesByLanguage_basic() {
        // Typical use case
        assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
    }

}