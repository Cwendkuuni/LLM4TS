import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertEquals(null, LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageCountry() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHash_ShouldThrowException() {
        LocaleUtils.toLocale("en#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThanTwoCharacters_ShouldThrowException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_FirstCharacterNotUnderscore_ShouldThrowException() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseAndTwoCharacters_ShouldThrowException() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseAndThreeCharacters_ShouldThrowException() {
        LocaleUtils.toLocale("eng");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndTwoUpperCaseCountry_ShouldThrowException() {
        LocaleUtils.toLocale("en_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndThreeUpperCaseCountry_ShouldThrowException() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndEmptyCountryAndNonEmptyVariant_ShouldThrowException() {
        LocaleUtils.toLocale("en__variant");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThanThreeCharactersAfterUnderscore_ShouldThrowException() {
        LocaleUtils.toLocale("_U");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LastCharacterAfterUnderscoreNotUpperCase_ShouldThrowException() {
        LocaleUtils.toLocale("_us");
    }

    @Test
    public void testToLocale_ValidFormatWithUnderscoreAndTwoCharactersAfterUnderscore_ReturnLocaleWithEmptyLanguageAndCountry() {
        assertEquals(new Locale("", "us"), LocaleUtils.toLocale("_US"));
    }

    @Test
    public void testToLocale_ValidFormatWithUnderscoreAndTwoCharactersAfterUnderscoreAndNonEmptyVariant_ReturnLocaleWithEmptyLanguageAndCountry() {
        assertEquals(new Locale("", "us", "variant"), LocaleUtils.toLocale("_US_variant"));
    }

    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnEmptyList() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_WithLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnNonEmptyList() {
        assertTrue(LocaleUtils.availableLocaleList().size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnNonEmptySet() {
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_WithNonAvailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr")));
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnEmptyList() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_WithCountryCode_ReturnListOfLanguages() {
        List<Locale> expected = Arrays.asList(new Locale("en", ""), new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnEmptyList() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_WithLanguageCode_ReturnListOfCountries() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
}