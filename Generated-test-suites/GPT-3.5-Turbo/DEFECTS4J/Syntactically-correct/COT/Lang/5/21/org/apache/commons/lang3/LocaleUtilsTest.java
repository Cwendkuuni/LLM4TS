import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Testing valid input
        assert(LocaleUtils.toLocale("en").equals(new Locale("en", "")));
        assert(LocaleUtils.toLocale("en_GB").equals(new Locale("en", "GB")));
        assert(LocaleUtils.toLocale("en_GB_xxx").equals(new Locale("en", "GB", "xxx")));

        // Testing invalid input
        try {
            LocaleUtils.toLocale(null);
            assert(false);  // should throw an exception
        } catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            LocaleUtils.toLocale("e");  // invalid locale format
            assert(false);  // should throw an exception
        } catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            LocaleUtils.toLocale("ENG");  // invalid locale format (country code should be uppercase)
            assert(false);  // should throw an exception
        } catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            LocaleUtils.toLocale("en_gb");  // invalid locale format (separator should be underscore)
            assert(false);  // should throw an exception
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        List<Locale> expectedList1 = new ArrayList<>();
        expectedList1.add(new Locale("fr", "CA", "xxx"));
        expectedList1.add(new Locale("fr", "CA"));
        expectedList1.add(new Locale("fr"));
        expectedList1.add(new Locale("en"));

        assert(LocaleUtils.localeLookupList(locale1).equals(expectedList1));

        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("fr", "CA", "xxx"));
        expectedList2.add(new Locale("fr", "CA"));
        expectedList2.add(new Locale("fr"));
        expectedList2.add(new Locale("en"));

        assert(LocaleUtils.localeLookupList(locale1, locale2).equals(expectedList2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assert(availableLocales.size() > 0);  // should not be empty
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assert(availableLocales.size() > 0);  // should not be empty
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assert(LocaleUtils.isAvailableLocale(availableLocales.get(0)));
        assert(!LocaleUtils.isAvailableLocale(new Locale("xyz")));  // should not be available
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
        assert(languagesByCountry.isEmpty());

        languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assert(languagesByCountry.size() > 0);  // should not be empty
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assert(countriesByLanguage.isEmpty());

        countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        assert(countriesByLanguage.size() > 0);  // should not be empty
    }

}