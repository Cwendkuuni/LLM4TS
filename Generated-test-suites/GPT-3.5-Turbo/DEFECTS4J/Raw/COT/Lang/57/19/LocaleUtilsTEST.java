
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_ValidFormat() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), result);

        result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), result);

        result = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("enGB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguage() {
        LocaleUtils.toLocale("EN_GB");
    }

    // ... More test cases for LocaleUtils.toLocale() method

    @Test
    public void testLocaleLookupList_OnlyLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_Variant() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        Assert.assertEquals(expected, result);
    }

    // ... More test cases for LocaleUtils.localeLookupList() method

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        // Assuming that there are some available locales
        Assert.assertNotEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        // Assuming that there are some available locales
        Assert.assertNotEquals(0, result.size());
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.US);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("xx", "YY"));
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("gd", "GB")
        );
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "CA"),
                new Locale("en", "AU")
        );
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("XX");
        Assert.assertEquals(Collections.emptyList(), result);
    }
}

