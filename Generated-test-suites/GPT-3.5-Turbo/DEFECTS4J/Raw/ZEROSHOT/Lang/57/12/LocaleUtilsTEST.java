import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleString() {
        String localeString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);
    }
    
    @Test
    public void testToLocale_NullLocaleString() {
        String localeString = null;
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertNull(actualLocale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat() {
        String localeString = "abc";
        LocaleUtils.toLocale(localeString);
    }
    
    @Test
    public void testLocaleLookupList_StartFromLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testLocaleLookupList_StartFromLocaleWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
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
        Set<Locale> expectedSet = new HashSet<>(LocaleUtils.availableLocaleList());
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, actualSet);
    }
    
    @Test
    public void testIsAvailableLocale_KnownLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_UnknownLocale() {
        Locale locale = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        String countryCode = "GB";
        List<Locale> expectedList = Arrays.asList(
            new Locale("en", "GB"),
            new Locale("cy", "GB")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        String countryCode = null;
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertTrue(actualList.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA"),
            new Locale("fr", "CH"),
            new Locale("fr", "BE"),
            new Locale("fr", "LU"),
            new Locale("fr", "MC"),
            new Locale("fr", "GF"),
            new Locale("fr", "PF"),
            new Locale("fr", "RE"),
            new Locale("fr", "YT"),
            new Locale("fr", "MQ")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        String languageCode = null;
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertTrue(actualList.isEmpty());
    }

}