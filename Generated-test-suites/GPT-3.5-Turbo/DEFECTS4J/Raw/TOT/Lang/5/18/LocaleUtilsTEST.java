import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NormalInput_ReturnsLocaleObject() {
        // Arrange
        String input = "en_GB";

        // Act
        Locale locale = LocaleUtils.toLocale(input);

        // Assert
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_LowercaseLanguage_ReturnsLocaleObject() {
        // Arrange
        String input = "fr";

        // Act
        Locale locale = LocaleUtils.toLocale(input);

        // Assert
        Assert.assertEquals("fr", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_UppercaseCountry_ReturnsLocaleObject() {
        // Arrange
        String input = "EN_US";

        // Act
        Locale locale = LocaleUtils.toLocale(input);

        // Assert
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_NullInput_ThrowsIllegalArgumentException() {
        // Arrange
        String input = null;

        // Act
        LocaleUtils.toLocale(input);

        // Assert (Exception expected)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        // Arrange
        String input = "eng";

        // Act
        LocaleUtils.toLocale(input);

        // Assert (Exception expected)
    }

    @Test
    public void testLocaleLookupList_SpecificLocale_ReturnsListOfLocalesFromSpecificToDefault() {
        // Arrange
        Locale locale = new Locale("fr", "CA");

        // Act
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Assert
        Assert.assertEquals(4, localeList.size());
        Assert.assertEquals(new Locale("fr", "CA", ""), localeList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeList.get(1));
        Assert.assertEquals(new Locale("fr"), localeList.get(2));
        Assert.assertEquals(Locale.getDefault(), localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale_ReturnsListOfLocalesFromDefault() {
        // Arrange
        Locale locale = Locale.getDefault();

        // Act
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Assert
        Assert.assertEquals(1, localeList.size());
        Assert.assertEquals(locale, localeList.get(0));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        // Arrange
        Locale locale = null;

        // Act
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Assert
        Assert.assertEquals(0, localeList.size());
    }

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableListOfAvailableLocales() {
        // Arrange

        // Act
        List<Locale> localeList = LocaleUtils.availableLocaleList();

        // Assert
        Assert.assertNotNull(localeList);
        try {
            localeList.add(Locale.US);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSetOfAvailableLocales() {
        // Arrange

        // Act
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();

        // Assert
        Assert.assertNotNull(localeSet);
        try {
            localeSet.add(Locale.US);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        // Arrange
        Locale locale = Locale.US;

        // Act
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        // Assert
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        // Arrange
        Locale locale = new Locale("xx", "YY");

        // Act
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        // Assert
        Assert.assertFalse(isAvailable);
    }
    
    @Test
    public void testLanguagesByCountry_KnownCountry_ReturnsListOfLanguages() {
        // Arrange
        String countryCode = "US";

        // Act
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        Assert.assertEquals(1, languages.size());
        Assert.assertEquals(Locale.US, languages.get(0));
    }

    @Test
    public void testLanguagesByCountry_UnknownCountry_ReturnsEmptyList() {
        // Arrange
        String countryCode = "XX";

        // Act
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        Assert.assertEquals(0, languages.size());
    }

    @Test
    public void testCountriesByLanguage_KnownLanguage_ReturnsListOfCountries() {
        // Arrange
        String languageCode = "en";

        // Act
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        Assert.assertEquals(1, countries.size());
        Assert.assertEquals(Locale.US, countries.get(0));
    }

    @Test
    public void testCountriesByLanguage_UnknownLanguage_ReturnsEmptyList() {
        // Arrange
        String languageCode = "xx";

        // Act
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        Assert.assertEquals(0, countries.size());
    }
}