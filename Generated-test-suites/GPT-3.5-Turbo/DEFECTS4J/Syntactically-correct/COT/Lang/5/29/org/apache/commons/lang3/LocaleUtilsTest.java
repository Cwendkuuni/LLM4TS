import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");

        Assert.assertEquals(new Locale("en", ""), locale1);
        Assert.assertEquals(new Locale("en", "GB"), locale2);
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xxx_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        List<Locale> expectedList = Collections.singletonList(new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(null, new Locale("en"));

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualList = LocaleUtils.availableLocaleList();

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();

        Assert.assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsNotAvailableLocale() {
        Locale locale = new Locale("zz", "ZZ");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>(
                Arrays.asList(new Locale("en", "US"), new Locale("en", "GB")));
        List<Locale> actualList = LocaleUtils.languagesByCountry("US");

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry(null);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>(
                Arrays.asList(new Locale("en", "US"), new Locale("fr", "CA")));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage(null);

        Assert.assertEquals(expectedList, actualList);
    }

}

