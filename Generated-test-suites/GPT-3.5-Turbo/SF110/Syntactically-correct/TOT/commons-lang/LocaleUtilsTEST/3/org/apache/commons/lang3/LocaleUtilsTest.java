import org.junit.*;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_NullOutput() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_EmptyInput_CorrectOutput() {
        Locale result = LocaleUtils.toLocale("");
        Assert.assertEquals(new Locale("", ""), result);
    }

    @Test
    public void testToLocale_InvalidFormat_ExceptionThrown() {
        try {
            LocaleUtils.toLocale("invalid_format");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: invalid_format", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat2Chars_CorrectOutput() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en"), result);
    }

    @Test
    public void testToLocale_ValidFormat3Chars_CorrectOutput() {
        Locale result = LocaleUtils.toLocale("eng");
        Assert.assertEquals(new Locale("eng"), result);
    }

    @Test
    public void testToLocale_ValidFormat2Chars_CorrectOutputWithCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocale_ValidFormat2CharsUpperCase_CorrectOutputWithCountry() {
        Locale result = LocaleUtils.toLocale("EN_US");
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocale_ValidFormat2CharsLowerCase_ExceptionThrown() {
        try {
            LocaleUtils.toLocale("eb");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: eb", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat3CharsLowerCase_ExceptionThrown() {
        try {
            LocaleUtils.toLocale("engb");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: engb", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat3Chars_CorrectOutputWithCountry() {
        Locale result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), result);
    }

    @Test
    public void testToLocale_ValidFormat3CharsUpperCase_CorrectOutputWithCountry() {
        Locale result = LocaleUtils.toLocale("EN_GB");
        Assert.assertEquals(new Locale("en", "GB"), result);
    }

    @Test
    public void testToLocale_ValidFormatExtended_CorrectOutputWithScript() {
        Locale result = LocaleUtils.toLocale("zh_Hans_CN");
        Assert.assertEquals(new Locale("zh", "Hans", "CN"), result);
    }

    @Test
    public void testToLocale_ValidFormatExtendedLowerCase_ExceptionThrown() {
        try {
            LocaleUtils.toLocale("zh_hans_cn");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: zh_hans_cn", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormatExtendedUpperCase_CorrectOutputWithScript() {
        Locale result = LocaleUtils.toLocale("ZH_HANS_CN");
        Assert.assertEquals(new Locale("zh", "Hans", "CN"), result);
    }

    @Test
    public void testLocaleLookupList_NullInput_EmptyResult() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale_ContainsLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(locale));
    }

    @Test
    public void testLocaleLookupList_ValidInput_ContainsAllLocales() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(defaultLocale));
        Assert.assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        Assert.assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
    }

    @Test
    public void testAvailableLocaleList_ValidInput_ContainsAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ValidInput_ContainsAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = Locale.getDefault();
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_EmptyResult() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode_EmptyResult() {
        List<Locale> result = LocaleUtils.languagesByCountry("INVALID_COUNTRY");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ContainsLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.stream().allMatch(locale -> countryCode.equals(locale.getCountry()) && locale.getVariant().isEmpty()));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_EmptyResult() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_EmptyResult() {
        List<Locale> result = LocaleUtils.countriesByLanguage("INVALID_LANGUAGE");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ContainsCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.stream().allMatch(locale -> languageCode.equals(locale.getLanguage()) && !locale.getCountry().isEmpty() && locale.getVariant().isEmpty()));
    }
}