import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        Assert.assertEquals(expected, actual);

        expected = new Locale("es", "");
        actual = LocaleUtils.toLocale("es");
        Assert.assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException with null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException with invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en");
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US");
        expected = Arrays.asList(locale, new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx_xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("en", "US", "xx_xx_xx_xx_xx_xx_xx_xx_xx");
        expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<Locale>(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        list.add(new Locale("en", "US", "xx"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        set.add(new Locale("en", "US", "xx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry() {
        try {
            LocaleUtils.languagesByCountry(null);
            Assert.fail("Expected IllegalArgumentException with null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            List<Locale> actual = LocaleUtils.languagesByCountry("XX");
            Assert.assertEquals(Collections.emptyList(), actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
            List<Locale> actual = LocaleUtils.languagesByCountry("GB");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"), new Locale("fr", "BE"));
            List<Locale> actual = LocaleUtils.languagesByCountry("CA");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"));
            List<Locale> actual = LocaleUtils.languagesByCountry("ES");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage() {
        try {
            LocaleUtils.countriesByLanguage(null);
            Assert.fail("Expected IllegalArgumentException with null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            List<Locale> actual = LocaleUtils.countriesByLanguage("XX");
            Assert.assertEquals(Collections.emptyList(), actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
            List<Locale> actual = LocaleUtils.countriesByLanguage("en");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
            List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }

        try {
            List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"), new Locale("es", "CL"), new Locale("es", "CO"));
            List<Locale> actual = LocaleUtils.countriesByLanguage("es");
            Assert.assertEquals(expected, actual);
        } catch (IllegalArgumentException e) {
            // Not expected
        }
    }

}