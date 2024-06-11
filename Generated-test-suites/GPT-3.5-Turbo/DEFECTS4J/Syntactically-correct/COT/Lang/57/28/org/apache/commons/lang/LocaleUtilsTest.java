import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid_format");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");

        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedLocaleList, actualLocaleList);

        List<Locale> defaultLocaleList = Arrays.asList(new Locale("en"));
        actualLocaleList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        expectedLocaleList.addAll(defaultLocaleList);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "locale")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLanguages = Arrays.asList(new Locale("fr", "CA"));
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLanguages, actualLanguages);

        expectedLanguages = Arrays.asList(new Locale("en", "GB"));
        actualLanguages = LocaleUtils.languagesByCountry("GB");
        assertEquals(expectedLanguages, actualLanguages);

        assertTrue(LocaleUtils.languagesByCountry("invalid_country_code").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedCountries = Arrays.asList(new Locale("fr", "CA"));
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expectedCountries, actualCountries);

        expectedCountries = Arrays.asList(new Locale("en", "GB"));
        actualCountries = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedCountries, actualCountries);

        assertTrue(LocaleUtils.countriesByLanguage("invalid_language_code").isEmpty());
    }
}