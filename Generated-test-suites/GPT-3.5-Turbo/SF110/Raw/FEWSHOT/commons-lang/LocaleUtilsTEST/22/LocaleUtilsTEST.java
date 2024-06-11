import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "posix"), LocaleUtils.toLocale("en_US_posix"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsException2() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleThrowsException3() {
        LocaleUtils.toLocale("_");
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en")));
        
        expected = Arrays.asList(new Locale("en"), new Locale("en", "US"), new Locale("en", ""));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en"), Locale.US));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = LocaleUtils.cLanguagesByCountry.get("US");
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = LocaleUtils.cCountriesByLanguage.get("en");
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}