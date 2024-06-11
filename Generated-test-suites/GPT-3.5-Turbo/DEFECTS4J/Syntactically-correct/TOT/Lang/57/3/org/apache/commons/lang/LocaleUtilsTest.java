import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale1.getLanguage());
        assertEquals("GB", locale1.getCountry());

        // Edge case with only language code
        Locale locale2 = LocaleUtils.toLocale("fr");
        assertEquals("fr", locale2.getLanguage());
        assertEquals("", locale2.getCountry());
    }
    
    @Test
    public void testLocaleLookupList() {
        // Typical use case with non-null locale and default locale
        Locale locale1 = LocaleUtils.toLocale("fr_CA_xxx");
        Locale defaultLocale = LocaleUtils.toLocale("en");
        List<Locale> resultList1 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        assertEquals(4, resultList1.size());
        assertEquals(locale1, resultList1.get(0));
        assertEquals(LocaleUtils.toLocale("fr_CA"), resultList1.get(1));
        assertEquals(LocaleUtils.toLocale("fr"), resultList1.get(2));
        assertEquals(defaultLocale, resultList1.get(3));
        
        // Edge case with null locale and default locale
        List<Locale> resultList2 = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, resultList2.size());
        assertEquals(defaultLocale, resultList2.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> resultList = LocaleUtils.availableLocaleList();
        assertNotNull(resultList);
        assertTrue(resultList.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
        assertNotNull(resultSet);
        assertTrue(resultSet.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale() {
        // Typical use case with existing locale
        Locale locale1 = LocaleUtils.toLocale("en_US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        // Typical use case with non-existing locale
        Locale locale2 = LocaleUtils.toLocale("xx_XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Typical use case with existing country code
        List<Locale> resultList1 = LocaleUtils.languagesByCountry("CA");
        assertNotNull(resultList1);
        assertTrue(resultList1.size() > 0);
        
        // Typical use case with non-existing country code
        List<Locale> resultList2 = LocaleUtils.languagesByCountry("XX");
        assertNotNull(resultList2);
        assertTrue(resultList2.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Typical use case with existing language code
        List<Locale> resultList1 = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(resultList1);
        assertTrue(resultList1.size() > 0);
        
        // Typical use case with non-existing language code
        List<Locale> resultList2 = LocaleUtils.countriesByLanguage("xx");
        assertNotNull(resultList2);
        assertTrue(resultList2.isEmpty());
    }
}