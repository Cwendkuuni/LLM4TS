import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {
    
    List<Locale> availableLocales;
    
    @Before
    public void setUp() {
        availableLocales = Arrays.asList(Locale.getAvailableLocales());
    }
    
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testLocaleLookupList_locale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupList_localeAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_nullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<>(availableLocales);
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(availableLocales);
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        assertEquals(new ArrayList<>(), LocaleUtils.languagesByCountry(null));
        
        List<Locale> expectedList = new ArrayList<>();
        for (Locale locale : availableLocales) {
            if (locale.getCountry().equals("CA") && locale.getVariant().isEmpty()) {
                expectedList.add(locale);
            }
        }
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        assertEquals(new ArrayList<>(), LocaleUtils.countriesByLanguage(null));
        
        List<Locale> expectedList = new ArrayList<>();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals("fr")
                    && !locale.getCountry().isEmpty()
                    && locale.getVariant().isEmpty()) {
                expectedList.add(locale);
            }
        }
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }
}