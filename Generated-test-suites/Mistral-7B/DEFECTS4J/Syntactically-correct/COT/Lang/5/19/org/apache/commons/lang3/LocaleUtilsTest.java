import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyString() {
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
        LocaleUtils.toLocale("en_GB_xy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LanguageCodeWithUppercase() {
        LocaleUtils.toLocale("En_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_CountryCodeWithLowercase() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testLocaleLookupList_DefaultLocaleIsInput() {
        Locale locale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet_ValidInput() {
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
        Locale locale = new Locale("ab", "ab");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "ES"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        String countryCode = null;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), actual);
    }

}