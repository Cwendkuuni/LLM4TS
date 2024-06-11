import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Locale;
import org.junit.Test;
public class LocaleUtilsTest {
    @Test
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testToLocaleLanguageCode() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }
    
    @Test
    public void testToLocaleLanguageCountryCode() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("", locale.getVariant());
    }
    
    @Test
    public void testToLocaleLanguageCountryVariantCode() {
        Locale locale = LocaleUtils.toLocale("en_US_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }
    
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertEquals(0, localeList.size());
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertTrue(localeList.contains(locale));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr")));
    }
    
    @Test
    public void testLocaleLookupListDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, localeList.size());
        assertTrue(localeList.contains(locale));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr")));
        assertTrue(localeList.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(localeList.size(), LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocaleAvailable() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testLanguagesByCountryNullCountryCode() {
        List<Locale> langList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, langList.size());
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langList = LocaleUtils.languagesByCountry("US");
        assertTrue(langList.size() > 0);
        for (Locale locale : langList) {
            assertEquals("US", locale.getCountry());
            assertEquals("", locale.getVariant());
        }
    }
    
    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countryList.size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertTrue(countryList.size() > 0);
        for (Locale locale : countryList) {
            assertEquals("en", locale.getLanguage());
            assertEquals("", locale.getVariant());
        }
    }
}