import org.junit.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleWithValidString() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidString() {
        LocaleUtils.toLocale("en_GB_xxx");
    }

    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = new Locale("en");
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("xyz");
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(actual);
    }

    @Test
    public void testLanguagesByCountryWithValidCountryCode() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = new ArrayList<>();
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (int i = 0; i < locales.size(); i++) {
            Locale locale = locales.get(i);
            if ("CA".equals(locale.getCountry()) && locale.getVariant().length() == 0) {
                actual.add(locale);
            }
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> expected = LocaleUtils.languagesByCountry("XYZ");
        List<Locale> actual = new ArrayList<>();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageWithValidLanguageCode() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("fr");
        List<Locale> actual = new ArrayList<>();
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (int i = 0; i < locales.size(); i++) {
            Locale locale = locales.get(i);
            if ("fr".equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().length() == 0) {
                actual.add(locale);
            }
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("XYZ");
        List<Locale> actual = new ArrayList<>();
        Assert.assertEquals(expected, actual);
    }

}