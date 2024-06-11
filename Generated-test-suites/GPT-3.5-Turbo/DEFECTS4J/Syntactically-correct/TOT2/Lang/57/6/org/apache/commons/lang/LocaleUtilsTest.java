import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocaleUtilsTest {

    private LocaleUtils localeUtils;

    @Before
    public void setUp() {
        localeUtils = new LocaleUtils();
    }

    @Test
    public void testToLocaleValidInput() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleInvalidInput() {
        assertNull(LocaleUtils.toLocale("invalid"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLanguages = Arrays.asList(new Locale("en", "US"));
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedCountries = Arrays.asList(new Locale("en", "US"));
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedCountries, actualCountries);
    }

}