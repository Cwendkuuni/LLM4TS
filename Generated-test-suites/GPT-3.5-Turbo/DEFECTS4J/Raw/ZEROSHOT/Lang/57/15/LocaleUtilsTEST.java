import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        assertNull(locale1.getVariant());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertNull(locale2.getVariant());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
        
        Locale locale4 = LocaleUtils.toLocale(null);
        assertNull(locale4);
        
        try {
            Locale locale5 = LocaleUtils.toLocale("en-GB");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
        
        try {
            Locale locale6 = LocaleUtils.toLocale("ENG_GB");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
        
        try {
            Locale locale7 = LocaleUtils.toLocale("en_GB_UK");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale1 = LocaleUtils.localeLookupList(Locale.ENGLISH);
        List<Locale> expectedList1 = Collections.unmodifiableList(Arrays.asList(Locale.ENGLISH));
        assertEquals(expectedList1, locale1);
        
        Locale locale2 = LocaleUtils.localeLookupList(Locale.CANADA_FRENCH, Locale.ENGLISH);
        List<Locale> expectedList2 = Collections.unmodifiableList(Arrays.asList(Locale.CANADA_FRENCH, Locale.CANADA, Locale.FRENCH, Locale.ENGLISH));
        assertEquals(expectedList2, locale2);
        
        Locale locale3 = LocaleUtils.localeLookupList(Locale.JAPAN, Locale.FRENCH);
        List<Locale> expectedList3 = Collections.unmodifiableList(Arrays.asList(Locale.JAPAN, Locale.JAPANESE, Locale.FRENCH));
        assertEquals(expectedList3, locale3);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.CANADA_FRENCH));
        assertTrue(list.contains(Locale.JAPAN));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.CANADA_FRENCH));
        assertTrue(set.contains(Locale.JAPAN));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA_FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINA));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages1 = LocaleUtils.languagesByCountry("GB");
        List<Locale> expectedLanguages1 = Collections.unmodifiableList(Arrays.asList(Locale.ENGLISH));
        assertEquals(expectedLanguages1, languages1);
        
        List<Locale> languages2 = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguages2 = Collections.unmodifiableList(Arrays.asList(Locale.FRENCH));
        assertEquals(expectedLanguages2, languages2);
        
        List<Locale> languages3 = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, languages3);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountries1 = Collections.unmodifiableList(Arrays.asList(Locale.CANADA, Locale.UK, Locale.US));
        assertEquals(expectedCountries1, countries1);
        
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedCountries2 = Collections.unmodifiableList(Arrays.asList(Locale.CANADA_FRENCH));
        assertEquals(expectedCountries2, countries2);
        
        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, countries3);
    }
}