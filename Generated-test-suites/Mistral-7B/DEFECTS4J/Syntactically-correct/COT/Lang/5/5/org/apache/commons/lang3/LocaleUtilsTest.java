import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public void testToLocale_InvalidLanguageCode() {
        LocaleUtils.toLocale("AbCd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode() {
        LocaleUtils.toLocale("en_ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSeparator() {
        LocaleUtils.toLocale("en_GB_xyz");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength() {
        LocaleUtils.toLocale("en_GB____");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testLocaleLookupList_ValidInputWithDefaultLocale() {
        Locale expected = new Locale("en");
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, expected);
        assertEquals(expected, actual.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        // Perform some random checks on the list using assertions
        // to ensure that the list is not empty and contains some locales
        assertTrue(actual.size() > 0);
        assertNotNull(actual.get(0));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("es", "MX")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_InvalidCountryCode() {
        LocaleUtils.languagesByCountry("AB");
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("en", "US"),
                new Locale("fr", "FR"),
                new Locale("fr", "CA")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_InvalidLanguageCode() {
        LocaleUtils.countriesByLanguage("AB");
    }
}