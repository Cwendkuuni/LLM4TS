
import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidString_ReturnsLocaleObject() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_LocaleIsNull_ReturnsEmptyList() {
        Assert.assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testLocaleLookupList_LocaleIsNotNull_ReturnsCorrectList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        Assert.assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnsCorrectList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsCorrectSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_LocaleIsAvailable_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_LocaleIsNotAvailable_ReturnsFalse() {
        Locale locale = new Locale("xx", "YY");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_CountryCodeIsNull_ReturnsEmptyList() {
        Assert.assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    @Test
    public void testLanguagesByCountry_CountryCodeIsNotNull_ReturnsCorrectList() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", "GB"));
        Assert.assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeIsNull_ReturnsEmptyList() {
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeIsNotNull_ReturnsCorrectList() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("US"));
        expectedList.add(new Locale("GB"));
        Assert.assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
    }

}
