import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic(){
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_countryCodeUpperCase(){
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_variant(){
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat(){
        Locale locale = LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList(){
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertEquals(4, locales.size());
        assertEquals(Locale.US, locales.get(0));
        assertEquals(Locale.US, locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));
        assertEquals(Locale.ENGLISH, locales.get(3));
    }

    @Test
    public void testLocaleLookupList_nullLocale(){
        List<Locale> locales = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertEquals(1, locales.size());
        assertEquals(Locale.ENGLISH, locales.get(0));
    }

    @Test
    public void testAvailableLocaleList(){
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet(){
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale(){
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("qq", "QQ")));
    }

    @Test
    public void testLanguagesByCountry(){
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertTrue(languages.size() > 0);
    }

    @Test
    public void testCountriesByLanguage(){
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertTrue(countries.size() > 0);
    }
}