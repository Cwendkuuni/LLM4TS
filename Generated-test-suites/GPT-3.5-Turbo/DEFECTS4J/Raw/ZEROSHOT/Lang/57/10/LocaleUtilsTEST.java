import org.junit.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.ENGLISH, locale);
        
        locale = LocaleUtils.toLocale("fr_FR");
        assertEquals(Locale.FRENCH, locale);
        
        locale = LocaleUtils.toLocale("es_ES_xxx");
        assertEquals(new Locale("es", "ES", "xxx"), locale);
        
        locale = LocaleUtils.toLocale("xx_XX_xxx");
        assertEquals(new Locale("xx", "XX", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(lookupList.contains(new Locale("fr", "CA")));
        assertTrue(lookupList.contains(new Locale("fr")));
        assertTrue(lookupList.contains(Locale.getDefault()));
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(lookupList.contains(new Locale("fr", "CA")));
        assertTrue(lookupList.contains(new Locale("fr")));
        assertTrue(lookupList.contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        assertNotNull(languages);
        assertTrue(languages.contains(Locale.ENGLISH));
        
        languages = LocaleUtils.languagesByCountry("XX");
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertTrue(countries.contains(Locale.CANADA));
        
        countries = LocaleUtils.countriesByLanguage("xx");
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

}