import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

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
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(list, result);
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(list, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.contains(new Locale("en")));
        assertTrue(locales.contains(new Locale("fr")));
        // ... add more checks for specific locales ...
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.contains(new Locale("en")));
        assertTrue(locales.contains(new Locale("fr")));
        // ... add more checks for specific locales ...
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        Locale locale3 = new Locale("de", "DE");
        assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs1 = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected1 = Arrays.asList(
            new Locale("en", "CA"),
            new Locale("fr", "CA")
        );
        assertEquals(expected1, langs1);

        List<Locale> langs2 = LocaleUtils.languagesByCountry("DE");
        List<Locale> expected2 = Arrays.asList();
        assertEquals(expected2, langs2);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected1 = Arrays.asList(
            new Locale("CA"),
            new Locale("GB"),
            new Locale("US")
        );
        assertEquals(expected1, countries1);

        List<Locale> countries2 = LocaleUtils.countriesByLanguage("de");
        List<Locale> expected2 = Arrays.asList(new Locale("DE"));
        assertEquals(expected2, countries2);
    }
}