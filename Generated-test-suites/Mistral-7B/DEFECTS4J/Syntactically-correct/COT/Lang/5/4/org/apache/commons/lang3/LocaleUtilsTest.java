import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        Assert.assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        Assert.assertEquals(expected2, actual2);

        String input3 = "en_GB_xxx";
        Locale expected3 = new Locale("en", "GB", "xxx");
        Locale actual3 = LocaleUtils.toLocale(input3);
        Assert.assertEquals(expected3, actual3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_EmptyString() {
        String input = "";
        Locale actual = LocaleUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidLanguageCode() {
        String input = "xyz";
        Locale actual = LocaleUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidCountryCode() {
        String input = "en_xyz";
        Locale actual = LocateUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidSeparator() {
        String input = "en_GB_xyz";
        Locale actual = LocaleUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_LengthLessThanTwo() {
        String input = "en";
        Locale actual = LocaleUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_LengthGreaterThanFive() {
        String input = "en_GB_xyz_zzz";
        Locale actual = LocaleUtils.toLocale(input);
        Assert.assertNull(actual);
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        String inputLocale = "fr_CA_XXX";
        Locale givenLocale = new Locale("fr", "CA", "XXX");
        List<Locale> result = LocaleUtils.localeLookupList(givenLocale);

        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "XXX"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        Locale inputLocale = null;
        List<Locale> result = LocaleUtils.localeLookupList(inputLocale);

        List<Locale> expected = Collections.emptyList();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        int size = result.size();
        Assert.assertEquals(true, size > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        int size = result.size();
        Assert.assertEquals(true, size > 0);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        String inputLocaleString = "en_US";
        Locale inputLocale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(inputLocale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        String inputLocaleString = "en_XX";
        Locale inputLocale = new Locale("en", "XX");
        boolean result = LocaleUtils.isAvailableLocale(inputLocale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        int size = result.size();
        Assert.assertEquals(true, size > 0);
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        int size = result.size();
        Assert.assertEquals(true, size > 0);
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(Collections.emptyList(), result);
    }
}