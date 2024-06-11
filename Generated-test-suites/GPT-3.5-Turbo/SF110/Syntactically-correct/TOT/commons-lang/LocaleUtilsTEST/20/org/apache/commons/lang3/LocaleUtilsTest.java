
package org.apache.commons.lang3;

import org.junit.Test;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLanguageOnly_ReturnsLocaleWithLanguageOnly() {
        Locale result = LocaleUtils.toLocale("en");

        assertEquals(Locale.ENGLISH, result);
    }

    @Test
    public void testToLocale_ValidLanguageAndCountry_ReturnsLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");

        assertEquals(Locale.US, result);
    }

    @Test
    public void testToLocale_ValidLanguageCountryAndVariant_ReturnsLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_variant");

        assertEquals(new Locale("en", "US", "variant"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidShortLocaleFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidShortCountryFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_U");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidEmptyCountryFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidEmptyCountryAndVariantFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en__");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSingleCharacterCountryFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_U");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidShortVariantFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_v");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidEmptyVariantFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidShortFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_");
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListWithLocaleAndRelatedLocales() {
        Locale locale = Locale.CANADA_FRENCH;

        List<Locale> result = LocaleUtils.localeLookupList(locale);

        assertEquals(4, result.size());
        assertEquals(Locale.CANADA_FRENCH, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr", ""), result.get(2));
        assertEquals(Locale.ENGLISH, result.get(3));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocaleWithEmptyVariant_ReturnsListWithLocaleAndRelatedLocales() {
        Locale locale = new Locale("fr", "CH", "");

        List<Locale> result = LocaleUtils.localeLookupList(locale);

        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CH"), result.get(1));
        assertEquals(new Locale("fr", ""), result.get(2));
        assertEquals(Locale.ENGLISH, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnsNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();

        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();

        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_ValidAvailableLocale_ReturnsTrue() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.CANADA);

        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_InvalidAvailableLocale_ReturnsFalse() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("zzz"));

        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        String countryCode = "US";

        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        String languageCode = "fr";

        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(Locale.CANADA_FRENCH));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);

        assertTrue(result.isEmpty());
    }
}
