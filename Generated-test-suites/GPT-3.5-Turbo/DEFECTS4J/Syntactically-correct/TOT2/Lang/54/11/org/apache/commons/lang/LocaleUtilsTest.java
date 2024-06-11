import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidString_ReturnLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidString_ThrowException() {
        Locale locale = LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList_StartLocale_ReturnList() {
        Locale startLocale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(startLocale);
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(startLocale, list.get(0));
        assertEquals("fr", list.get(1).getLanguage());
        assertEquals("CA", list.get(1).getCountry());
        assertEquals("fr", list.get(2).getLanguage());
    }

    @Test
    public void testAvailableLocaleList_ReturnList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.availableLocaleList(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnList() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnList() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
}