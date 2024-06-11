import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_EmptyInput_ReturnEmptyLocale() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("", ""), result);
    }

    @Test
    public void testToLocale_InvalidFormat_ExceptionThrown() {
        String str = "abc#def";
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: abc#def", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidTwoLetterLowercase_ReturnLocale() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en"), result);
    }

    @Test
    public void testToLocale_ValidThreeLetterLowercase_ReturnLocale() {
        String str = "eng";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("eng"), result);
    }

    @Test
    public void testToLocale_ValidTwoLetterLanguageAndTwoLetterCountry_ReturnLocale() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocale_ValidTwoLetterLanguageAndThreeLetterCountry_ReturnLocale() {
        String str = "en_USA";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", "USA"), result);
    }

    @Test
    public void testToLocale_ValidTwoLetterLanguageAndEmptyCountry_ReturnLocale() {
        String str = "en_";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", ""), result);
    }

    @Test
    public void testToLocale_ValidTwoLetterLanguageAndEmptyCountryWithVariant_ReturnLocale() {
        String str = "en__var";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", "", "var"), result);
    }

    @Test
    public void testToLocale_InvalidTwoLetterLanguageAndEmptyCountry_ExceptionThrown() {
        String str = "ab_";
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: ab_", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidUnderscoreLocale_ReturnLocale() {
        String str = "_US";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("", "US"), result);
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        Locale locale = null;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnList() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en"));
        expected.add(new Locale("", "US"));
        expected.add(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_ValidLocaleWithVariant_ReturnList() {
        Locale locale = new Locale("en", "US", "var");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US", "var"));
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en"));
        expected.add(new Locale("", "US"));
        expected.add(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_NullAndValidLocale_ReturnList() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = new ArrayList<>();
        expected.add(defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_ReturnList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet_ReturnSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnList() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnList() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSyncAvoid() {
        // Nothing to test in this inner class
    }
}