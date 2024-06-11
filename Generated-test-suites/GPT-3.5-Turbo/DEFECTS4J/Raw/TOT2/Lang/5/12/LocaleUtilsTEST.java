import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_ValidInput() {
        Locale result = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidInput() {
        try {
            Locale result = LocaleUtils.toLocale("invalid");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, resultList.size());
        // add more assertions if needed
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> resultList = LocaleUtils.availableLocaleList();
        Assert.assertFalse(resultList.isEmpty());
        // add more assertions if needed
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
        Assert.assertFalse(resultSet.isEmpty());
        // add more assertions if needed
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> resultList = LocaleUtils.languagesByCountry("US");
        Assert.assertFalse(resultList.isEmpty());
        // add more assertions if needed
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> resultList = LocaleUtils.countriesByLanguage("en");
        Assert.assertFalse(resultList.isEmpty());
        // add more assertions if needed
    }
}