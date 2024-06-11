import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en_GB_xxx_x");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: en_GB_xxx_x", ex.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidInput_ReturnLocale() {
        Locale result = LocaleUtils.toLocale("en_GB");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("GB", result.getCountry());
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(Locale.ENGLISH, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnUnmodifiableList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result instanceof java.util.Collections.UnmodifiableList);
    }

    @Test
    public void testAvailableLocaleSet_ReturnUnmodifiableSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result instanceof java.util.Collections.UnmodifiableSet);
    }

    @Test
    public void testIsAvailableLocale_LocaleInAvailableList_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_LocaleNotInAvailableList_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnList() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Locale.CANADA, result.get(0));
        assertEquals(Locale.ENGLISH, result.get(1));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Locale.CANADA, result.get(0));
    }

}