import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

    // Test case for toLocale(String str) method
    @Test
    public void testToLocale() {
        // Typical use case
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        // Typical use case
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        // Typical use case
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        // Edge case: variant should be uppercase (JDK1.4+), so "xxx" should be converted to "XXX"
        Assert.assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));
        // Error scenario: invalid locale format, should throw IllegalArgumentException
        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
    }

    // Test case for localeLookupList(Locale locale) method
    @Test
    public void testLocaleLookupList() {
        // Typical use case
        List<Locale> expected1 = new ArrayList<Locale>();
        expected1.add(new Locale("en", "GB", "xxx"));
        expected1.add(new Locale("en", "GB"));
        expected1.add(new Locale("en"));
        expected1.add(Locale.getDefault());
        Assert.assertEquals(expected1, LocaleUtils.localeLookupList(new Locale("en", "GB", "xxx")));
        // Typical use case
        List<Locale> expected2 = new ArrayList<Locale>();
        expected2.add(new Locale("fr", "CA", "xxx"));
        expected2.add(new Locale("fr", "CA"));
        expected2.add(new Locale("fr"));
        expected2.add(new Locale("en"));
        Assert.assertEquals(expected2, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        // Edge case: null locale, should return empty list
        Assert.assertEquals(Collections.EMPTY_LIST, LocaleUtils.localeLookupList(null));
    }

    // Test case for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        // Verify that the list is unmodifiable
        try {
            locales.add(new Locale("de"));
            Assert.fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // exception expected
        }
    }

    // Test case for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(locales);
        // Verify that the set is unmodifiable
        try {
            locales.add(new Locale("de"));
            Assert.fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // exception expected
        }
    }

    // Test case for isAvailableLocale(Locale locale) method
    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        // Typical use case
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
        // Error scenario: null locale, should return false
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    // Test case for languagesByCountry(String countryCode) method
    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        Assert.assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        // Edge case: null countryCode, should return empty list
        Assert.assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry(null));
    }

    // Test case for countriesByLanguage(String languageCode) method
    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("US", "en"));
        expected.add(new Locale("IN", "en"));
        Assert.assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        // Edge case: null languageCode, should return empty list
        Assert.assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage(null));
    }
}