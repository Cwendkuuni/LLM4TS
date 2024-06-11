import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_gB"); // lowercase 'B'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguage() {
        LocaleUtils.toLocale("xx_YY"); // invalid language 'xx'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountry() {
        LocaleUtils.toLocale("en_XX"); // invalid country 'XX'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en_GB_xx"); // invalid separator '_xx'
    }

    @Test
    public void testLocaleLookupList() {
        Locale expected = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(expected, null);
        List<Locale> expectedList = Arrays.asList(expected, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, actual);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale defaultLocale = new Locale("en");
        Locale expected = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(expected, defaultLocale);
        List<Locale> expectedList = Arrays.asList(expected, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("ca", "CA"), new Locale("us", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("ca");
        assertEquals(expected, actual);
    }
}