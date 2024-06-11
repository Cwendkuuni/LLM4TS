import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);

        try {
            LocaleUtils.toLocale("");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, resultList);

        locale = new Locale("en");
        resultList = LocaleUtils.localeLookupList(locale);
        expectedList.remove(new Locale("en"));
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
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA"));

        List<Locale> resultList = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expectedList, resultList);

        resultList = LocaleUtils.languagesByCountry("");
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));

        List<Locale> resultList = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedList, resultList);

        resultList = LocaleUtils.countriesByLanguage("");
        Assert.assertTrue(resultList.isEmpty());
    }
}