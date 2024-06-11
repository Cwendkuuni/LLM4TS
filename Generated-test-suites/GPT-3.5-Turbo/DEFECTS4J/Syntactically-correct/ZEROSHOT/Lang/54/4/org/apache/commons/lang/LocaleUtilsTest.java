import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(locale1, new Locale("en", ""));

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(locale2, new Locale("en", "GB"));

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(locale3, new Locale("en", "GB", "xxx"));

        Locale locale4 = LocaleUtils.toLocale("fr_CA");
        assertEquals(locale4, new Locale("fr", "CA"));

        Locale locale5 = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(locale5, new Locale("fr", "CA", "xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("en_GB_x");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("en", "GB", "xxx");
        Locale defaultLocale1 = new Locale("en");
        List<Locale> expected1 = Arrays.asList(locale1, new Locale("en", "GB"), new Locale("en"), defaultLocale1);
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1, defaultLocale1);
        assertEquals(expected1, result1);

        Locale locale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale2 = new Locale("en");
        List<Locale> expected2 = Arrays.asList(locale2, new Locale("fr", "CA"), new Locale("fr"), defaultLocale2);
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, defaultLocale2);
        assertEquals(expected2, result2);

        List<Locale> result3 = LocaleUtils.localeLookupList(null, defaultLocale2);
        assertEquals(Collections.singletonList(defaultLocale2), result3);

        List<Locale> result4 = LocaleUtils.localeLookupList(null, null);
        assertEquals(Collections.emptyList(), result4);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale invalidLocale = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected1 = Arrays.asList(new Locale("en"), new Locale("es"));
        List<Locale> result1 = LocaleUtils.languagesByCountry("US");
        assertEquals(expected1, result1);

        List<Locale> expected2 = Collections.singletonList(new Locale("fr"));
        List<Locale> result2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(expected2, result2);

        List<Locale> result3 = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected1 = Arrays.asList(new Locale("US"), new Locale("CA"));
        List<Locale> result1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected1, result1);

        List<Locale> expected2 = Collections.singletonList(new Locale("FR"));
        List<Locale> result2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected2, result2);

        List<Locale> result3 = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result3);
    }

}