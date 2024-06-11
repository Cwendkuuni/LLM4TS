import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

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

        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
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
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("de", "DE", "xxx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("en"));
        actual = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("en"), new Locale("fr"));
        actual = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("US"));
        actual = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("CA"));
        actual = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("US"), new Locale("CA"));
        actual = LocaleUtils.countriesByLanguage("es");
        Assert.assertEquals(expected, actual);
    }
}