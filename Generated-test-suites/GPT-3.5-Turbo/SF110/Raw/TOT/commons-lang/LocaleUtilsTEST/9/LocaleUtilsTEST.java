import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNullLocale() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        Assert.assertEquals(new Locale("", ""), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterAtStart_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("*en_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterInLanguage_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e1_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLengthInLanguage_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("eng_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterInCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_U$");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLengthInCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterInVariant_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_@");
    }

    @Test
    public void testToLocale_ValidFormatWithLanguageOnly_ReturnsLocaleWithEmptyCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en"), result);
    }

    @Test
    public void testToLocale_ValidFormatWithLanguageAndCountry_ReturnsLocaleWithEmptyVariant() {
        Locale result = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocale_ValidFormatWithLanguageCountryAndVariant_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        Assert.assertEquals(new Locale("en", "US", "variant"), result);
    }

    @Test
    public void testToLocale_ValidFormatWithLanguageVariantOnly_ReturnsLocaleWithEmptyCountry() {
        Locale result = LocaleUtils.toLocale("en__variant");
        Assert.assertEquals(new Locale("en", "", "variant"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan3AndInvalidCharacterAtMultiplePlaces_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e$$");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan5_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterAtSecondPosition_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_e_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterAtThirdPosition_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCharacterAtFourthPosition_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("__1");
    }

    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_WithLocaleWithoutVariant_ReturnsListWithDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(new Locale("en")));
    }

    @Test
    public void testLocaleLookupList_WithLocaleWithVariant_ReturnsListWithDefaultLocaleAndParentLocale() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(new Locale("en", "US")));
        Assert.assertTrue(result.contains(new Locale("en")));
    }

    @Test
    public void testLocaleLookupList_WithLocaleWithoutCountry_ReturnsListWithDefaultLocale() {
        Locale locale = new Locale("en", "");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(new Locale("en")));
    }

    @Test
    public void testLocaleLookupList_WithDifferentDefaultLocale_ReturnsListWithDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(defaultLocale));
        Assert.assertTrue(result.contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList_ReturnsListWithAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            Assert.assertTrue(result.contains(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSetWithAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            Assert.assertTrue(result.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_WithNotAvailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xyz", "abc");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_WithNotAvailableCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("xyz");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_WithAvailableCountryCode_ReturnsListOfLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(new Locale("en", "US"), result.get(0));
    }

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithNotAvailableLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xyz");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithAvailableLanguageCode_ReturnsListOfCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(new Locale("en", "US"), result.get(0));
    }
}