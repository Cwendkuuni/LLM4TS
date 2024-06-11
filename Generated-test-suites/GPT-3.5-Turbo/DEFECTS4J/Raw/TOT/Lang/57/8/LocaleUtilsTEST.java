import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidString() {
        // Typical use case: converting "en_GB" to Locale object
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assert actual.equals(expected);

        // Edge case: converting "fr_FR" to Locale object
        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assert actual.equals(expected);

        // Error scenario: converting "abc" to Locale object, throws IllegalArgumentException
        try {
            LocaleUtils.toLocale("abc");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: getting locale lookup list for "en_GB"
        Locale locale = new Locale("en", "GB");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("en", "GB"),
                new Locale("en"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assert actual.equals(expected);

        // Edge case: getting locale lookup list for "fr_CA_xxx"
        locale = new Locale("fr", "CA", "xxx");
        defaultLocale = new Locale("en");
        expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assert actual.equals(expected);

        // Error scenario: getting locale lookup list for null locale
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assert actual.equals(expected);
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case: getting list of available locales
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assert actual.equals(expected);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case: getting set of available locales
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assert actual.equals(expected);
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: checking if "en_US" is available
        Locale locale = new Locale("en", "US");
        boolean expected = LocaleUtils.isAvailableLocale(locale);
        assert expected;

        // Edge case: checking if "de_DE" is available
        locale = new Locale("de", "DE");
        expected = LocaleUtils.isAvailableLocale(locale);
        assert expected;

        // Error scenario: checking if null locale is available
        expected = LocaleUtils.isAvailableLocale(null);
        assert !expected;
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: getting languages for country "GB"
        String countryCode = "GB";
        List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actual = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("gd", "GB"),
                new Locale("cy", "GB")
        );
        assert actual.equals(expected);

        // Edge case: getting languages for country "CA"
        countryCode = "CA";
        expected = LocaleUtils.languagesByCountry(countryCode);
        actual = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("iu", "CA")
        );
        assert actual.equals(expected);

        // Error scenario: getting languages for null country code
        countryCode = null;
        expected = LocaleUtils.languagesByCountry(countryCode);
        assert expected.isEmpty();
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: getting countries for language "en"
        String languageCode = "en";
        List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actual = Arrays.asList(
                new Locale("en", "ZA"),
                new Locale("en", "ZW"),
                new Locale("en", "TT"),
                new Locale("en", "TO"),
                new Locale("en", "TK"),
                new Locale("en", "TL"),
                new Locale("en", "KM"),
                new Locale("en", "US"),
                new Locale("en", "ER"),
                new Locale("en", "LS"),
                new Locale("en", "MH"),
                new Locale("en", "GU"),
                new Locale("en", "FJ"),
                new Locale("en", "AT"),
                new Locale("en", "BI"),
                new Locale("en", "KY"),
                new Locale("en", "VE"),
                new Locale("en", "DM"),
                new Locale("en", "GB"),
                new Locale("en", "SG")
        );
        assert actual.equals(expected);

        // Edge case: getting countries for language "fr"
        languageCode = "fr";
        expected = LocaleUtils.countriesByLanguage(languageCode);
        actual = Arrays.asList(
                new Locale("fr", "BE"),
                new Locale("fr", "CM"),
                new Locale("fr", "NE"),
                new Locale("fr", "MF"),
                new Locale("fr", "MG"),
                new Locale("fr", "TW"),
                new Locale("fr", "MU"),
                new Locale("fr", "PF"),
                new Locale("fr", "WF"),
                new Locale("fr", "BI"),
                new Locale("fr", "MG"),
                new Locale("fr", "MQ"),
                new Locale("fr", "BF"),
                new Locale("fr", "NE"),
                new Locale("fr", "CG"),
                new Locale("fr", "BE"),
                new Locale("fr", "TN"),
                new Locale("fr", "VI"),
                new Locale("fr", "MF"),
                new Locale("fr", "CM")
        );
        assert actual.equals(expected);

        // Error scenario: getting countries for null language code
        languageCode = null;
        expected = LocaleUtils.countriesByLanguage(languageCode);
        assert expected.isEmpty();
    }

}