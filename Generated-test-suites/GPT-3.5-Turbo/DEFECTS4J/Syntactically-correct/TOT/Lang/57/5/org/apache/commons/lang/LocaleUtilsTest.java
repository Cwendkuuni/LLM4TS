import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {

    private Locale locale;
    private Locale defaultLocale;

    @Before
    public void setUp() {
        locale = new Locale("fr", "CA", "xxx");
        defaultLocale = new Locale("en");
    }

    @After
    public void tearDown() {
        locale = null;
        defaultLocale = null;
    }

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_ValidLocaleString_ReturnLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testLocaleLookupList_SingleLocale_ReturnList() {
        assertEquals(3, LocaleUtils.localeLookupList(locale).size());
    }

    @Test
    public void testLocaleLookupList_MultipleLocales_ReturnList() {
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
    }

    @Test
    public void testAvailableLocaleList_ReturnList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleSet_ReturnSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
    }

    @Test
    public void testLanguagesByCountry_NullCountry_ReturnEmptyList() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountry_ReturnList() {
        assertFalse(LocaleUtils.languagesByCountry("CA").isEmpty());
        assertTrue(LocaleUtils.languagesByCountry("DE").isEmpty());
    }

    @Test
    public void testCountriesByLanguage_NullLanguage_ReturnEmptyList() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguage_ReturnList() {
        assertFalse(LocaleUtils.countriesByLanguage("fr").isEmpty());
        assertTrue(LocaleUtils.countriesByLanguage("de").isEmpty());
    }

}