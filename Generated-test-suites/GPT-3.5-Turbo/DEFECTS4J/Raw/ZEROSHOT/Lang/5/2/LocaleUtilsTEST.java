import org.junit.*;
import static org.junit.Assert.assertEquals;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);
    
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);
    
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    
        Locale locale4 = LocaleUtils.toLocale("fr");
        assertEquals(new Locale("fr", ""), locale4);
    
        Locale locale5 = LocaleUtils.toLocale("fr_CA");
        assertEquals(new Locale("fr", "CA"), locale5);
    
        Locale locale6 = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(new Locale("fr", "CA", "xxx"), locale6);
        
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: invalid", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: ", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");
        Locale locale3 = new Locale("fr", "CA");
        Locale locale4 = new Locale("fr");
    
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(Arrays.asList(locale1, locale3, locale4, locale2), list1);
    
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, locale4);
        assertEquals(Arrays.asList(locale2, locale4, locale1, locale3), list2);
        
        Locale locale5 = null;
        List<Locale> list3 = LocaleUtils.localeLookupList(locale5, locale4);
        assertEquals(Arrays.asList(locale4), list3);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), list);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertEquals(new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales())), set);
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "GB");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertEquals(true, result);
        
        Locale invalidLocale = new Locale("xx", "XX");
        boolean invalidResult = LocaleUtils.isAvailableLocale(invalidLocale);
        assertEquals(false, invalidResult);
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedLangs = new ArrayList<Locale>();
        
        expectedLangs.add(new Locale("fr", "CA", ""));
        
        assertEquals(expectedLangs, langs);
        
        List<Locale> invalidLangs = LocaleUtils.languagesByCountry("XX");
        assertEquals(Collections.emptyList(), invalidLangs);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedCountries = new ArrayList<Locale>();
        
        expectedCountries.add(new Locale("fr", "CA", ""));
        
        assertEquals(expectedCountries, countries);
        
        List<Locale> invalidCountries = LocaleUtils.countriesByLanguage("XX");
        assertEquals(Collections.emptyList(), invalidCountries);
    }
}