import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null_Input() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_Invalid_Format() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testToLocale_Language_Code_Only() {
        Locale result = LocaleUtils.toLocale("en");
        Locale expectedLocale = new Locale("en", "");
        assertEquals(expectedLocale, result);
    }

    @Test
    public void testToLocale_Language_Code_And_Country_Code() {
        Locale result = LocaleUtils.toLocale("en_GB");
        Locale expectedLocale = new Locale("en", "GB");
        assertEquals(expectedLocale, result);
    }

    @Test
    public void testToLocale_Language_Code_Country_Code_And_Variant() {
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        assertEquals(expectedLocale, result);
    }

    @Test
    public void testLocaleLookupList_Single_Locale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, result);
    }

    @Test
    public void testLocaleLookupList_Locale_And_Default_Locale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }

    @Test
    public void testIsAvailableLocale_Valid_Locale() {
        Locale locale = new Locale("en", "GB");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_Invalid_Locale() {
        Locale locale = new Locale("zz", "ZZ");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_Valid_Country_Code() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        List<Locale> expectedList = Arrays.asList(new Locale("de", "GB"), new Locale("en", "GB"), new Locale("es", "GB"), new Locale("fr", "GB"), new Locale("it", "GB"));
        assertEquals(expectedList, result);
    }

    @Test
    public void testLanguagesByCountry_Invalid_Country_Code() {
        List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_Valid_Language_Code() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "BE"), new Locale("fr", "BF"), new Locale("fr", "BI"), new Locale("fr", "BJ"), new Locale("fr", "BL"), new Locale("fr", "BM"), new Locale("fr", "BN"), new Locale("fr", "BO"), new Locale("fr", "BQ"), new Locale("fr", "BR"), new Locale("fr", "BS"), new Locale("fr", "BT"), new Locale("fr", "BV"), new Locale("fr", "BW"), new Locale("fr", "BY"), new Locale("fr", "BZ"), new Locale("fr", "CA"), new Locale("fr", "CB"), new Locale("fr", "CC"), new Locale("fr", "CD"), new Locale("fr", "CF"), new Locale("fr", "CG"), new Locale("fr", "CH"), new Locale("fr", "CI"), new Locale("fr", "CJ"), new Locale("fr", "CK"), new Locale("fr", "CL"), new Locale("fr", "CM"), new Locale("fr", "CN"), new Locale("fr", "CO"), new Locale("fr", "CP"), new Locale("fr", "CR"), new Locale("fr", "CS"), new Locale("fr", "CT"), new Locale("fr", "CU"), new Locale("fr", "CV"), new Locale("fr", "CW"), new Locale("fr", "CX"), new Locale("fr", "CY"), new Locale("fr", "CZ"), new Locale("fr", "DD"), new Locale("fr", "DE"), new Locale("fr", "DF"), new Locale("fr", "DG"), new Locale("fr", "DJ"), new Locale("fr", "DK"), new Locale("fr", "DM"), new Locale("fr", "DO"), new Locale("fr", "DZ"), new Locale("fr", "EA"), new Locale("fr", "EB"), new Locale("fr", "EC"), new Locale("fr", "ED"));
        assertEquals(expectedList, result);
    }

    @Test
    public void testCountriesByLanguage_Invalid_Language_Code() {
        List<Locale> result = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(result.isEmpty());
    }

}