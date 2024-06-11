import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Collections;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
        try {
            LocaleUtils.toLocale("#");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("_");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> expected = Collections.singletonList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        expected = Collections.singletonList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
        
        locale = new Locale("fr", "FR");
        defaultLocale = new Locale("fr", "CA");
        expected = Collections.singletonList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        expected = Collections.singletonList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
        
        locale = new Locale("es");
        defaultLocale = new Locale("es", "MX");
        expected = Collections.singletonList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        expected = Collections.singletonList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
        
        locale = new Locale("");
        defaultLocale = new Locale("pt", "BR");
        expected = Collections.singletonList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        expected = Collections.singletonList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "MX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("pt", "BR")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        String countryCode = null;
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
        
        countryCode = "US";
        expected = Collections.singletonList(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
        
        countryCode = "CA";
        expected = Collections.singletonList(new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
        
        countryCode = "MX";
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
        
        countryCode = "BR";
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }
    
    @Test
    public void testCountriesByLanguage() {
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
        
        languageCode = "en";
        expected = Collections.singletonList(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
        
        languageCode = "fr";
        expected = Collections.singletonList(new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
        
        languageCode = "es";
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
        
        languageCode = "pt";
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }
}