import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        Assert.assertEquals("", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithHashtag_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithLengthLessThan2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithNoUnderscoreAndInvalidLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testToLocale_ValidLocaleFormatWithNoUnderscoreAndLowerCaseLength2_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithOneUnderscoreAndInvalidLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithOneUnderscoreAndInvalidLength_ReturnsLocale() {
        LocaleUtils.toLocale("en_");
    }

    @Test
    public void testToLocale_ValidLocaleFormatWithOneUnderscoreAndLowerCaseLength2AndUpperCaseLength2_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscoreAndInvalidLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_");
    }
    
    @Test
    public void testToLocale_ValidLocaleFormatWithTwoUnderscoreAndLowerCaseLength2AndUpperCaseLength2AndNonEmptyRemaining_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US_test");
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
        Assert.assertEquals("test", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithLowerCaseUnderscoreAndInvalidLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithLowerCaseUnderscoreAndUpperCaseLength2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithLowerCaseUnderscoreAndUpperCaseLength2AndNonEmptyRemaining_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US_test");
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnsNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xyz", "XYZ");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListWithMatchingLocales() {
        String countryCode = "US";
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        for (Locale locale : result) {
            Assert.assertEquals(countryCode, locale.getCountry());
            Assert.assertTrue(locale.getVariant().isEmpty());
        }
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListWithMatchingLocales() {
        String languageCode = "en";
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        for (Locale locale : result) {
            Assert.assertEquals(languageCode, locale.getLanguage());
            Assert.assertFalse(locale.getCountry().isEmpty());
            Assert.assertTrue(locale.getVariant().isEmpty());
        }
    }

}