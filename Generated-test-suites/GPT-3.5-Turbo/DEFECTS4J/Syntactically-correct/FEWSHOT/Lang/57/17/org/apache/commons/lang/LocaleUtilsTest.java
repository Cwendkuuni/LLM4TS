import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("eng");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("EN");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expectedList, result);
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expectedList, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.getAvailableLocales());
        
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        assertEquals(expectedList, result);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>();
        expectedSet.add(Locale.getAvailableLocales());
        
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        assertEquals(expectedSet, result);
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "GB");
        
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA"));
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        assertEquals(expectedList, result);
    }
    
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA"));
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        assertEquals(expectedList, result);
    }
}