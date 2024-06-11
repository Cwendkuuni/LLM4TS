import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToLocale_validLocaleString_returnsLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleString_throwsIllegalArgumentException() {
        LocaleUtils.toLocale("enGB"); // no underscore
    }
    
    @Test
    public void testLocaleLookupList_singleLocale_returnsListStartingWithGivenLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = List.of(locale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupList_multipleLocales_returnsOrderedUnmodifiableList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList_returnsUnmodifiableList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        try {
            locales.add(new Locale("de"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet_returnsUnmodifiableSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        try {
            locales.add(new Locale("de"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testIsAvailableLocale_knownLocale_returnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }
    
    @Test
    public void testIsAvailableLocale_unknownLocale_returnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    }
    
    @Test
    public void testLanguagesByCountry_knownCountry_returnsListWithSupportedLanguages() {
        List<Locale> expectedList = List.of(new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }
    
    @Test
    public void testLanguagesByCountry_unknownCountry_returnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("DE"));
    }
    
    @Test
    public void testCountriesByLanguage_knownLanguage_returnsListWithSupportedCountries() {
        List<Locale> expectedList = List.of(new Locale("fr", "CA"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }
    
    @Test
    public void testCountriesByLanguage_unknownLanguage_returnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("de"));
    }

}