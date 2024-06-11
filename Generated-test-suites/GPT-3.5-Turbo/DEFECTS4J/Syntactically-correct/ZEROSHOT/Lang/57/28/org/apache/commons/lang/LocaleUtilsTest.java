import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en_us");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testToLocale_ValidFormat_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List list = LocaleUtils.localeLookupList(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testLocaleLookupList_WithLocale_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        try {
            list.add(new Locale("es", "AR"));
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        try {
            set.add(new Locale("es", "AR"));
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        List list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(new Locale("en", "US"), list.get(0));
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        List list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(new Locale("en", "US"), list.get(0));
    }
}