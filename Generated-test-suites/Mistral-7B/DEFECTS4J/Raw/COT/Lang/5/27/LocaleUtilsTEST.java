import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_CA", "fr_CA_XX"};
        for (String str : testCases) {
            Locale expected = new Locale(str.substring(0, 2), str.substring(3, str.length()));
            Locale actual = LocaleUtils.toLocale(str);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XX");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> testCases = LocaleUtils.localeLookupList(Locale.ENGLISH);
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH, Locale.getDefault());
        assertEquals(expected, testCases);

        testCases = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected, testCases);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("xx", "yy");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        assertEquals(expected, actual);

        actual = LocaleUtils.languagesByCountry("US");
        expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("US", "en"), new Locale("GB", "en"));
        assertEquals(expected, actual);

        actual = LocaleUtils.countriesByLanguage("es");
        expected = Collections.emptyList();
        assertEquals(expected, actual);
    }
}