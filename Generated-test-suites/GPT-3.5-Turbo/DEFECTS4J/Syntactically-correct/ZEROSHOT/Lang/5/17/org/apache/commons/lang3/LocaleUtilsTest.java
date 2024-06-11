import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB_"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("eng");
    }

    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotAvailableLocale() {
        Locale locale = new Locale("xxx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA"));
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(new ArrayList<Locale>(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA"));
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(new ArrayList<Locale>(), result);
    }
}