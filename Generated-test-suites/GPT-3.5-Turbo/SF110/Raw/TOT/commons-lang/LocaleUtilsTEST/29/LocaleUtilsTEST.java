
package org.apache.commons.lang3;

import org.junit.Test;

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void toLocale_NullString_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void toLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithHash_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthLessThan2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthLessThan3WhenStartingWithUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithoutUnderscoreAndOccurrence0_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthLessThan3InFirstPartWithOccurrence1_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithUpperCaseInTheSecondPartWithOccurrence1_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthEqualsToZeroInThirdPartWithOccurrence2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithUpperCaseInTheSecondPartWithOccurrence2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthEqualsToZeroOrGreaterThanZeroInFourthPartWithOccurrence2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US__");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithLengthLessThan3AndNotStartingWithUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithSecondCharNotUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormatWithThirdCharNotUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_eN");
    }

    @Test
    public void toLocale_ValidLocaleFormatWithLengthEqualsTo3_ReturnsLocaleWithEmptyLanguage() {
        Locale result = LocaleUtils.toLocale("_EN");
        assertEquals("", result.getLanguage());
        assertEquals("EN", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_ValidLocaleFormatWithLengthLessThan5_AndNotStartingWithUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("enUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_ValidLocaleFormatWithLengthLessThan5_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US_");
    }

    @Test
    public void toLocale_ValidLocaleFormatWithLengthEqualsTo5_ReturnsLocaleWithEmptyLanguage() {
        Locale result = LocaleUtils.toLocale("_US_EN");
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("EN", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_ValidLocaleFormatWithLengthLessThan7_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_ValidLocaleFormatWithNoUnderscoreAtFourthPosition_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US_ENA");
    }

    @Test
    public void toLocale_ValidLocaleFormatWithLengthEqualsTo7_ReturnsLocaleWithEmptyLanguage() {
        Locale result = LocaleUtils.toLocale("_US_EN_A");
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("EN", result.getVariant());
    }

    @Test
    public void localeLookupList_SingleLocaleInput_ReturnsListWithSingleLocale() {
        Locale locale = Locale.US;
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void localeLookupList_LocaleWithVariantInput_ReturnsListWithTwoLocales() {
        Locale locale = new Locale("en", "US", "variant");
        assertEquals(2, LocaleUtils.localeLookupList(locale).size());
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale(locale.getLanguage(), locale.getCountry())));
    }

    @Test
    public void localeLookupList_LocaleWithCountryInput_ReturnsListWithTwoLocales() {
        Locale locale = new Locale("en", "US");
        assertEquals(2, LocaleUtils.localeLookupList(locale).size());
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale(locale.getLanguage(), "")));
    }

    @Test
    public void localeLookupList_NullLocaleInput_ReturnsListWithDefaultLocale() {
        Locale defaultLocale = Locale.getDefault();
        assertEquals(Collections.singletonList(defaultLocale), LocaleUtils.localeLookupList(null, defaultLocale));
    }

    @Test
    public void localeLookupList_LocaleInputWithDefaultLocale_ReturnsListWithDefaultLocale() {
        Locale locale = Locale.US;
        Locale defaultLocale = Locale.US;
        assertEquals(Collections.singletonList(defaultLocale), LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void availableLocaleList_ReturnsNonEmptyList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void availableLocaleSet_ReturnsNonEmptySet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void isAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void isAvailableLocale_NonAvailableLocale_ReturnsFalse() {
        Locale locale = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void languagesByCountry_NullCountryCode_ReturnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void languagesByCountry_ValidCountryCode_ReturnsListWithLanguages() {
        String countryCode = "US";
        assertTrue(LocaleUtils.languagesByCountry(countryCode).contains(Locale.US));
    }

    @Test
    public void languagesByCountry_NonExistingCountryCode_ReturnsEmptyList() {
        String countryCode = "ZZ";
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void countriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void countriesByLanguage_ValidLanguageCode_ReturnsListWithCountries() {
        String languageCode = "en";
        assertTrue(LocaleUtils.countriesByLanguage(languageCode).contains(Locale.US));
    }

    @Test
    public void countriesByLanguage_NonExistingLanguageCode_ReturnsEmptyList() {
        String languageCode = "zz";
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(languageCode));
    }

}
