import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "US", "xxx"), LocaleUtils.toLocale("en_US_xxx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList1 = List.of(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList1, LocaleUtils.localeLookupList(locale1));

        Locale locale2 = new Locale("en");
        List<Locale> expectedList2 = List.of(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedList2, LocaleUtils.localeLookupList(locale1, locale2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet);
        assertFalse(localeSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

}