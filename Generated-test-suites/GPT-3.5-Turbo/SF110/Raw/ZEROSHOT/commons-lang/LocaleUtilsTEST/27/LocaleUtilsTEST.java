import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNullLocale() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LengthLessThanTwo_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("AB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ABC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab_AB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab_ABC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageDelimiter_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab#AB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFullFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_AB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFullLength_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ABC");
    }

    @Test
    public void testToLocale_ValidLanguage_ReturnLocaleWithLanguage() {
        Locale locale = LocaleUtils.toLocale("ab");
        assertEquals("ab", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("ab_AB");
        assertEquals("ab", locale.getLanguage());
        assertEquals("AB", locale.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageCountryVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("ab_AB_variant");
        assertEquals("ab", locale.getLanguage());
        assertEquals("AB", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_LocaleNull_ReturnEmptyList() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_ReturnLookupList() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", ""));
        expectedList.add(Locale.getDefault());
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, list);
    }

    @Test
    public void testLocaleLookupList_LocaleAndDefaultLocaleNotNull_ReturnLookupList() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", ""));
        expectedList.add(defaultLocale);
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, list);
    }

    @Test
    public void testAvailableLocaleList_ReturnAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_LIST, list);
    }

    @Test
    public void testAvailableLocaleSet_ReturnAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_SET, set);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = Locale.getDefault();
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeNull_ReturnEmptyList() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeNotNull_ReturnLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("es", "US"));
        expectedList.add(new Locale("en", "CA"));
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedList, list);
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeNull_ReturnEmptyList() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeNotNull_ReturnCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("en", "CA"));
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList, list);
    }

    
    // Write your additional test cases here
    
}