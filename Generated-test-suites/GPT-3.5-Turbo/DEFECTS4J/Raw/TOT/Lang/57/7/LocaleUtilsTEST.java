import org.junit.*;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput_ReturnsLocaleObject() {
        String validInput = "en_US";
        Locale expectedOutput = new Locale("en", "US");
        Locale output = LocaleUtils.toLocale(validInput);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_ExceptionThrown() {
        String invalidInput = "INVALID";
        Locale output = LocaleUtils.toLocale(invalidInput);
    }

    @Test
    public void testLocaleLookupList_LocaleObject_ReturnsListOfLocales() {
        Locale input = new Locale("fr", "CA", "xxx");
        List<Locale> expectedOutput = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        List<Locale> output = LocaleUtils.localeLookupList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        Locale input = null;
        List<Locale> expectedOutput = Collections.emptyList();
        List<Locale> output = LocaleUtils.localeLookupList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAvailableLocaleList_Always_ReturnsNonEmptyList() {
        List<Locale> output = LocaleUtils.availableLocaleList();
        assertFalse(output.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_Always_ReturnsNonEmptySet() {
        Set<Locale> output = LocaleUtils.availableLocaleSet();
        assertFalse(output.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale input = new Locale("en", "US");
        boolean output = LocaleUtils.isAvailableLocale(input);
        assertTrue(output);
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnsFalse() {
        Locale input = new Locale("xx", "XX");
        boolean output = LocaleUtils.isAvailableLocale(input);
        assertFalse(output);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLocales() {
        String countryCode = "US";
        List<Locale> expectedOutput = Arrays.asList(
            new Locale("en", "US")
        );
        List<Locale> output = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        String countryCode = null;
        List<Locale> expectedOutput = Collections.emptyList();
        List<Locale> output = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfLocales() {
        String languageCode = "en";
        List<Locale> expectedOutput = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "CA")
        );
        List<Locale> output = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        String languageCode = null;
        List<Locale> expectedOutput = Collections.emptyList();
        List<Locale> output = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedOutput, output);
    }
}