import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale1);

        // Edge case with only language code
        Locale locale2 = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale2);

        // Edge case with variant
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        if (System.getProperty("java.version").startsWith("1.3")) {
            Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
        } else {
            Assert.assertEquals(new Locale("en", "GB", "xxx".toUpperCase()), locale3);
        }

        // Error scenario with invalid format
        try {
            LocaleUtils.toLocale("en_gb");
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        List<Locale> locales1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        Assert.assertArrayEquals(new Locale[]{new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")}, locales1.toArray());

        // Edge case with only language
        List<Locale> locales2 = LocaleUtils.localeLookupList(new Locale("en"));
        Assert.assertArrayEquals(new Locale[]{new Locale("en")}, locales2.toArray());

        // Error scenario with null input
        List<Locale> locales3 = LocaleUtils.localeLookupList(null);
        Assert.assertTrue(locales3.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case with available locale
        boolean isAvailable1 = LocaleUtils.isAvailableLocale(new Locale("en", "GB"));
        Assert.assertTrue(isAvailable1);

        // Typical use case with unavailable locale
        boolean isAvailable2 = LocaleUtils.isAvailableLocale(new Locale("xx", "YY"));
        Assert.assertFalse(isAvailable2);
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case with valid country code
        List<Locale> languages1 = LocaleUtils.languagesByCountry("CA");
        Assert.assertFalse(languages1.isEmpty());

        // Edge case with invalid country code
        List<Locale> languages2 = LocaleUtils.languagesByCountry("XX");
        Assert.assertTrue(languages2.isEmpty());

        // Error scenario with null input
        List<Locale> languages3 = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(languages3.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case with valid language code
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertFalse(countries1.isEmpty());

        // Edge case with invalid language code
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("xx");
        Assert.assertTrue(countries2.isEmpty());

        // Error scenario with null input
        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(countries3.isEmpty());
    }

}