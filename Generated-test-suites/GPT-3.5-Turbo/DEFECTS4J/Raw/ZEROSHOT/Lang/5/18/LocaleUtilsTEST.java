import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(Locale.UK, locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));

        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, resultList);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));

        List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, resultList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> resultList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, resultList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, resultSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsNotAvailableLocale() {
        Locale locale = new Locale("xx", "XX");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH);
        List<Locale> resultList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, resultList);
    }

    @Test
    public void testLanguagesByNotExistentCountry() {
        String countryCode = "XX";
        List<Locale> resultList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(Collections.emptyList(), resultList);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH);
        List<Locale> resultList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, resultList);
    }

    @Test
    public void testCountriesByNotExistentLanguage() {
        String languageCode = "xx";
        List<Locale> resultList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(Collections.emptyList(), resultList);
    }
}