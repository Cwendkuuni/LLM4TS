import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xxx"); // invalid variant
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Locale expected = new Locale("fr", "CA");
        Locale actual = localeList.get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Locale expected = new Locale("fr", "CA");
        Locale actual = localeList.get(0);
        assertEquals(expected, actual);
        Locale expectedDefault = defaultLocale;
        Locale actualDefault = localeList.get(localeList.size() - 1);
        assertEquals(expectedDefault, actualDefault);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("US");
        assertFalse(localeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("en");
        assertFalse(localeList.isEmpty());
    }

}