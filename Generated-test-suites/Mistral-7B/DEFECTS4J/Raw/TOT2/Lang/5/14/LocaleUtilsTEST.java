import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_LanguageCodeUppercase() {
        LocaleUtils.toLocale("en_GB_EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_LanguageCodeLength() {
        LocaleUtils.toLocale("en_GB_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_CountryCodeLength() {
        LocaleUtils.toLocale("en_gB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_Separator() {
        LocaleUtils.toLocale("en_GB_xxx");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidInput_DefaultLocale() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupList_InvalidInput_Locale() {
        LocaleUtils.localeLookupList(null, new Locale("en"));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        String countryCode = null;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actual.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_InvalidInput() {
        String countryCode = "";
        LocaleUtils.languagesByCountry(countryCode);
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("GB"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        String languageCode = null;
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actual.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_InvalidInput() {
        String languageCode = "";
        LocaleUtils.countriesByLanguage(languageCode);
    }

}