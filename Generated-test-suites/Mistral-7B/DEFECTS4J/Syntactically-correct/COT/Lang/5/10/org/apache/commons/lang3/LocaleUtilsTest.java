import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enGB"); // missing '_'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguage() {
        LocaleUtils.toLocale("xx_XX"); // invalid language code
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountry() {
        LocaleUtils.toLocale("en_xx"); // invalid country code
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleTooShort() {
        LocaleUtils.toLocale("en"); // missing country code
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleTooLong() {
        LocaleUtils.toLocale("en_US_xxxx"); // extra characters
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr","CA","xxx"),
                new Locale("fr","CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testLocaleLookupListNullLocale() {
        LocaleUtils.localeLookupList(null, new Locale("en"));
    }

    @Test(expected = NullPointerException.class)
    public void testLocaleLookupListNullDefaultLocale() {
        LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocaleFalse() {
        Locale locale = new Locale("xx", "XX", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryEmpty() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageEmpty() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }

}