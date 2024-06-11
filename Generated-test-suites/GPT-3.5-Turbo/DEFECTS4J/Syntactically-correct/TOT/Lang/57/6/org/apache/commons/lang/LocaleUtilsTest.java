import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_withValidInput_returnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(Locale.UK, locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withInvalidInput_throwsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_withLocale_returnsListWithLocale() {
        Locale locale = Locale.FRANCE;
        List<Locale> expectedList = Collections.singletonList(locale);
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_withLocaleAndDefaultLocale_returnsListWithLocaleAndDefaultLocale() {
        Locale locale = Locale.FRANCE;
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList_returnsListOfAvailableLocales() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.ENGLISH);
        expectedList.add(Locale.FRENCH);
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet_returnsSetOfAvailableLocales() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.ENGLISH);
        expectedList.add(Locale.FRENCH);
        List<Locale> actualList = new ArrayList<>(LocaleUtils.availableLocaleSet());
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale_withAvailableLocale_returnsTrue() {
        Locale locale = Locale.ENGLISH;
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_withUnavailableLocale_returnsFalse() {
        Locale locale = Locale.GERMAN;
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_withValidCountryCode_returnsListOfLanguages() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.ENGLISH);
        List<Locale> actualList = LocaleUtils.languagesByCountry("GB");
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_withInvalidCountryCode_returnsEmptyList() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry("ZZ");
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_withValidLanguageCode_returnsListOfCountries() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.UK);
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_withInvalidLanguageCode_returnsEmptyList() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage("zz");
        Assert.assertEquals(expectedList, actualList);
    }
}