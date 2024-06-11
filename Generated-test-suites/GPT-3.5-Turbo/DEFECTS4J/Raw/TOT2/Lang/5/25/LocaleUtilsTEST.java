import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void toLocale_ValidString_ReturnsLocaleObject() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void localeLookupList_SpecificLocale_ReturnsList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void availableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        try {
            result.add(new Locale("test")); // Attempting to modify the unmodifiable list
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception caught as expected
        }
    }

    @Test
    public void availableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        try {
            result.add(new Locale("test")); // Attempting to modify the unmodifiable set
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception caught as expected
        }
    }

    @Test
    public void isAvailableLocale_KnownLocale_ReturnsTrue() {
        Locale locale = new Locale("en");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void languagesByCountry_ValidCountryCode_ReturnsList() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(result);
    }

    @Test
    public void countriesByLanguage_ValidLanguageCode_ReturnsList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(result);
    }

}