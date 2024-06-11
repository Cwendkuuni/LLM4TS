import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals(Locale.ENGLISH, locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals(Locale.UK, locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("eng"); // Invalid format
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertNotNull(localeList);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));

        localeList = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertNotNull(localeList);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertNotNull(localeList);
        assertFalse(localeList.isEmpty());
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
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        assertNotNull(languages);
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.UK));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertTrue(countries.contains(Locale.UK));
        assertTrue(countries.contains(Locale.US));
    }

}