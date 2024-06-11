import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(LocaleUtils.toLocale(null), null);
        assertEquals(LocaleUtils.toLocale("en"), new Locale("en", ""));
        assertEquals(LocaleUtils.toLocale("en_GB"), new Locale("en", "GB"));
        assertEquals(LocaleUtils.toLocale("en_GB_xxx"), new Locale("en", "GB", "xxx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List expected = new ArrayList();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        assertEquals(LocaleUtils.localeLookupList(locale), expected);
        assertEquals(LocaleUtils.localeLookupList(locale, defaultLocale), expected);

        locale = new Locale("fr", "CA");
        expected = new ArrayList();
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        assertEquals(LocaleUtils.localeLookupList(locale), expected);
        assertEquals(LocaleUtils.localeLookupList(locale, defaultLocale), expected);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales, Locale.getAvailableLocales());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocales, new HashSet(LocaleUtils.availableLocaleList()));
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        assertEquals(LocaleUtils.isAvailableLocale(locale), true);
        locale = new Locale("xy");
        assertEquals(LocaleUtils.isAvailableLocale(locale), false);
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(languagesByCountry, Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(countriesByLanguage, Arrays.asList(new Locale("en", "CA"), new Locale("en", "GB"),
                                                          new Locale("en", "US")));
    }

}