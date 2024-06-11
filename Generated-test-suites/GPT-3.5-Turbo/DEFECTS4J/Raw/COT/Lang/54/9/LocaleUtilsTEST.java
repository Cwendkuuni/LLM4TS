import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, localeList.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(new Locale("en"), localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, availableLocales.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, availableLocales.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, languagesByCountry.size());
        assertTrue(languagesByCountry.contains(new Locale("en", "CA")));
        assertTrue(languagesByCountry.contains(new Locale("fr", "CA")));
        assertTrue(languagesByCountry.contains(new Locale("iu", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(150, countriesByLanguage.size());
        assertTrue(countriesByLanguage.contains(new Locale("en", "US")));
        assertTrue(countriesByLanguage.contains(new Locale("en", "GB")));
        assertTrue(countriesByLanguage.contains(new Locale("en", "CA")));
    }
}