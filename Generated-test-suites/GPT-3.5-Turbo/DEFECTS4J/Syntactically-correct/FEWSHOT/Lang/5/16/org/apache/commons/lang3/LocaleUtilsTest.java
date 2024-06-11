import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr_xxx"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, result);
        
        expected.clear();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), result);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales())), result);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);
        
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        result = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLanguagesByCountry_null() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
        
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_null() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
}