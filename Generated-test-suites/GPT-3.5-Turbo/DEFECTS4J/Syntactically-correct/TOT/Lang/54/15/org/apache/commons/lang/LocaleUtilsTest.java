import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    // test for toLocale() method
    @Test
    public void testToLocale() {
        // typical use cases
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
        
        // edge cases
        Locale locale4 = LocaleUtils.toLocale(null);
        assertNull(locale4);
        
        Locale locale5 = LocaleUtils.toLocale("");
        assertNotNull(locale5);
        assertEquals(new Locale(""), locale5);
        
        Locale locale6 = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), locale6);
        
        // error scenarios
        try {
            LocaleUtils.toLocale("en-US");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    // test for localeLookupList() method
    @Test
    public void testLocaleLookupList() {
        // typical use cases
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertNotNull(list1);
        assertEquals(3, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
        
        Locale locale2 = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2);
        assertNotNull(list2);
        assertEquals(2, list2.size());
        assertEquals(locale2, list2.get(0));
        assertEquals(new Locale("en"), list2.get(1));
        
        // edge cases
        List<Locale> list3 = LocaleUtils.localeLookupList(null);
        assertNotNull(list3);
        assertEquals(0, list3.size());
        
        List<Locale> list4 = LocaleUtils.localeLookupList(new Locale(""));

        assertNotNull(list1);
        assertEquals(1, list4.size());
        assertEquals(new Locale(""), list4.get(0));
    }
    
    // test for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
    
    // test for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        Locale locale2 = new Locale("xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
        
        Locale locale3 = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }
    
    // test for languagesByCountry() method
    @Test
    public void testLanguagesByCountry() {
        // typical use cases
        List<Locale> list1 = LocaleUtils.languagesByCountry("CA");
        assertNotNull(list1);
        assertFalse(list1.isEmpty());
        assertTrue(list1.contains(new Locale("en", "CA")));
        assertTrue(list1.contains(new Locale("fr", "CA")));
        
        // edge cases
        List<Locale> list2 = LocaleUtils.languagesByCountry("");
        assertNotNull(list2);
        assertTrue(list2.isEmpty());
        
        List<Locale> list3 = LocaleUtils.languagesByCountry(null);
        assertNotNull(list3);
        assertTrue(list3.isEmpty());
    }
    
    // test for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage() {
        // typical use cases
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list1);
        assertFalse(list1.isEmpty());
        assertTrue(list1.contains(new Locale("en", "US")));
        assertTrue(list1.contains(new Locale("en", "GB")));
        
        // edge cases
        List<Locale> list2 = LocaleUtils.countriesByLanguage("");
        assertNotNull(list2);
        assertTrue(list2.isEmpty());
        
        List<Locale> list3 = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list3);
        assertTrue(list3.isEmpty());
    }
}