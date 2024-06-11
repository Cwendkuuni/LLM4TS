import org.junit.Test;
import org.junit.Assert;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Assert.assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        // Edge case
        Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        // Error scenarios
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
        try {
            LocaleUtils.toLocale("_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
        try {
            LocaleUtils.toLocale("eN");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
        try {
            LocaleUtils.toLocale("en_US#");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> expected = Arrays.asList(locale, new Locale("en"), new Locale("en", ""), defaultLocale);
        Assert.assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
        // Edge case
        locale = new Locale("en");
        expected = Arrays.asList(locale, new Locale("en"), new Locale("en", ""));
        Assert.assertEquals(expected, LocaleUtils.localeLookupList(locale));
        // Error scenarios
        try {
            LocaleUtils.localeLookupList(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        Assert.assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        Assert.assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        // Error scenarios
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expected = Arrays.asList(Locale.US, Locale.CANADA);
        Assert.assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        // Edge case
        expected = Collections.emptyList();
        Assert.assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expected = Arrays.asList(Locale.US, Locale.CANADA);
        Assert.assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        // Edge case
        expected = Collections.emptyList();
        Assert.assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
}