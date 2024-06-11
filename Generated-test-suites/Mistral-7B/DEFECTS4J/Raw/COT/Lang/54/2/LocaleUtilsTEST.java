import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality tests
        assertNotNull(LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertNotNull(LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.forLanguageTag("en-GB"), LocaleUtils.toLocale("en_GB"));
        assertNotNull(LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(Locale.forLanguageTag("en-GB-xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge cases and exceptions
        assertNull(LocaleUtils.toLocale(null));
        assertFalse(LocaleUtils.toLocale("").isEmpty());
        assertFalse(LocaleUtils.toLocale("aa").isAvailableLocale());
        assertFalse(LocaleUtils.toLocale("AA_AA").isAvailableLocale());
        assertFalse(LocaleUtils.toLocale("aa_AA_XXX").isAvailableLocale());
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality tests
        List localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        List expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertSame(expectedList, localeList);

        // Edge cases and exceptions
        localeList = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        expectedList = Collections.emptyList();
        assertSame(expectedList, localeList);

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, null);
        expectedList = Arrays.asList(Locale.FRENCH);
        assertSame(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List localeList = LocaleUtils.availableLocaleList();
        List expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        assertSame(expectedList, localeList);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_JP));
    }

    @Test
    public void testLanguagesByCountry() {
        List languageList = LocaleUtils.languagesByCountry("CA");
        List expectedList = Arrays.asList(Locale.FRENCH, Locale.ENGLISH);
        assertSame(expectedList, languageList);
    }

    @Test
    public void testCountriesByLanguage() {
        List countryList = LocaleUtils.countriesByLanguage("en");
        List expectedList = Arrays.asList(Locale.CANADA, Locale.UNITED_STATES, Locale.AUSTRALIA);
        assertSame(expectedList, countryList);
    }
}