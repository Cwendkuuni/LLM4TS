import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test cases
        assert LocaleUtils.toLocale("en").equals(new Locale("en", ""));
        assert LocaleUtils.toLocale("en_GB").equals(new Locale("en", "GB"));
        assert LocaleUtils.toLocale("en_GB_xxx").equals(new Locale("en", "GB", "xxx"));

        // Exception handling scenarios
        try {
            LocaleUtils.toLocale(null);
            assert false; // Should have thrown an exception
        } catch (IllegalArgumentException e) {
            assert true; // Exception is expected
        }

        try {
            LocaleUtils.toLocale("abc");
            assert false; // Should have thrown an exception
        } catch (IllegalArgumentException e) {
            assert true; // Exception is expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        List<Locale> expected1 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2);
        List<Locale> expected2 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"));

        assert LocaleUtils.localeLookupList(locale1).equals(expected1);
        assert LocaleUtils.localeLookupList(locale1, locale2).equals(expected1);
        assert LocaleUtils.localeLookupList(locale2).equals(expected2);
        assert LocaleUtils.localeLookupList(null).isEmpty();
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assert LocaleUtils.availableLocaleList().equals(expected);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        assert LocaleUtils.availableLocaleSet().equals(expected);
    }

    @Test
    public void testIsAvailableLocale() {
        assert LocaleUtils.isAvailableLocale(Locale.getDefault());
        assert !LocaleUtils.isAvailableLocale(new Locale("xyz"));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"));
        assert LocaleUtils.languagesByCountry("CA").equals(expected);
        assert LocaleUtils.languagesByCountry(null).isEmpty();
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"));
        assert LocaleUtils.countriesByLanguage("fr").equals(expected);
        assert LocaleUtils.countriesByLanguage(null).isEmpty();
    }

}