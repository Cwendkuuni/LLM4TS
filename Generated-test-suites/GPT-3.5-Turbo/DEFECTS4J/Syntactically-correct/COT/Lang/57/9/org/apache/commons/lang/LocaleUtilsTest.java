import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("e");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        List<Locale> expected1 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2);
        Assert.assertEquals(expected1, LocaleUtils.localeLookupList(locale1, locale2));

        List<Locale> expected2 = Arrays.asList(locale2);
        Assert.assertEquals(expected2, LocaleUtils.localeLookupList(locale2, locale2));

        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, locale2));
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected1 = Arrays.asList(new Locale("fr", "CA"));
        Assert.assertEquals(expected1, LocaleUtils.languagesByCountry("CA"));

        List<Locale> expected2 = Arrays.asList();
        Assert.assertEquals(expected2, LocaleUtils.languagesByCountry("XX"));

        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected1 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        Assert.assertEquals(expected1, LocaleUtils.countriesByLanguage("en"));

        List<Locale> expected2 = Arrays.asList();
        Assert.assertEquals(expected2, LocaleUtils.countriesByLanguage("xx"));

        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}