
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en__GB");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.ENGLISH
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.GERMAN;
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = SyncAvoid.AVAILABLE_LOCALE_LIST;
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = SyncAvoid.AVAILABLE_LOCALE_SET;
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        boolean isAvailableEnLocale = LocaleUtils.isAvailableLocale(Locale.ENGLISH);
        Assert.assertTrue(isAvailableEnLocale);
    }

    @Test
    public void testIsAvailableLocaleInvalid() {
        boolean isAvailableInvalidLocale = LocaleUtils.isAvailableLocale(new Locale("abc"));
        Assert.assertFalse(isAvailableInvalidLocale);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry("GB");
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountryInvalid() {
        List<Locale> actualList = LocaleUtils.languagesByCountry("XYZ");
        Assert.assertTrue(actualList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguageInvalid() {
        List<Locale> actualList = LocaleUtils.countriesByLanguage("XYZ");
        Assert.assertTrue(actualList.isEmpty());
    }
}
