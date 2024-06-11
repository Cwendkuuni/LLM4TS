import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

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
            LocaleUtils.toLocale("eN");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected1 = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        Assert.assertEquals(expected1, LocaleUtils.localeLookupList(locale1, defaultLocale));

        Locale locale2 = null;
        List<Locale> expected2 = Collections.singletonList(defaultLocale);
        Assert.assertEquals(expected2, LocaleUtils.localeLookupList(locale2, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        Assert.assertEquals(expected, LocaleUtils.languagesByCountry("CA"));

        List<Locale> expected2 = Collections.emptyList();
        Assert.assertEquals(expected2, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "CA"));
        Assert.assertEquals(expected, LocaleUtils.countriesByLanguage("en"));

        List<Locale> expected2 = Collections.emptyList();
        Assert.assertEquals(expected2, LocaleUtils.countriesByLanguage(null));
    }
}