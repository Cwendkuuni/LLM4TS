import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale locale = LocaleUtils.toLocale(null);
        assert locale == null;
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assert locale.getLanguage().equals("") && locale.getCountry().equals("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHash_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThanTwo_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNonUpperCaseLanguageAndCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNonUpperCaseCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingLanguageOrCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en__abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThanThreeAndStartingWithUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }

    @Test
    public void testToLocale_ValidFormat_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assert locale.getLanguage().equals("en") && locale.getCountry().equals("US");
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assert localeList.isEmpty();
    }

    @Test
    public void testLocaleLookupList_WithDefaultLocale_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assert localeList.contains(locale) && localeList.contains(defaultLocale);
    }

    @Test
    public void testAvailableLocaleList_ReturnsNonNullList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assert localeList != null;
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonNullSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assert localeSet != null;
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        assert isAvailable;
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "YY");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        assert !isAvailable;
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assert languages.isEmpty();
    }

    @Test
    public void testLanguagesByCountry_ExistingCountryCode_ReturnsList() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assert !languages.isEmpty();
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assert countries.isEmpty();
    }

    @Test
    public void testCountriesByLanguage_ExistingLanguageCode_ReturnsList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assert !countries.isEmpty();
    }
}