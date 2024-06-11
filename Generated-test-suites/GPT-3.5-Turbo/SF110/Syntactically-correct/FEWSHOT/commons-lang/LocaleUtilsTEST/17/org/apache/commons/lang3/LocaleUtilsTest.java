import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en__"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertNull(LocaleUtils.toLocale("#"));
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(null);
        assertEquals(expected1, LocaleUtils.localeLookupList(null));
        
        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("en"));
        expected2.add(new Locale("en", "US"));
        expected2.add(new Locale("en", ""));
        expected2.add(new Locale("fr", "CA"));
        assertEquals(expected2, LocaleUtils.localeLookupList(new Locale("en", "CA"), new Locale("fr", "CA")));
        
        List<Locale> expected3 = new ArrayList<>();
        expected3.add(new Locale("en", "US"));
        expected3.add(new Locale("en", ""));
        expected3.add(new Locale("fr", "CA"));
        expected3.add(new Locale("fr", ""));
        assertEquals(expected3, LocaleUtils.localeLookupList(new Locale("en", "CA"), new Locale("fr")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", ""));
        expected.add(new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>();
        expected.add(new Locale("en", ""));
        expected.add(new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en", "")));
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("es", "")));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", ""));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        
        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("fr", ""));
        assertEquals(expected2, LocaleUtils.languagesByCountry("CA"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("GB"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("fr", "CA"));
        assertEquals(expected2, LocaleUtils.countriesByLanguage("fr"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("es"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

}