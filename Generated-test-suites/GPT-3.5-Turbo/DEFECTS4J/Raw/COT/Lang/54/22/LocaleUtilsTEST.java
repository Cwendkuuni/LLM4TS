
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_ValidLocaleString_ReturnsLocaleObject() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsException() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test
    public void testLocaleLookupList_SingleLocale_ReturnsListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List expectedList = Arrays.asList(locale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale_ReturnsListWithDefaultLocale() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("fr");
        List expectedList = Arrays.asList(locale, defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List expectedList = Collections.unmodifiableList(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set expectedSet = Collections.unmodifiableSet(new HashSet(Arrays.asList(Locale.getAvailableLocales())));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List expectedList = Collections.EMPTY_LIST;
        assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsList() {
        List expectedList = Collections.singletonList(new Locale("fr", "CA", "xxx"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List expectedList = Collections.EMPTY_LIST;
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsList() {
        List expectedList = Collections.singletonList(new Locale("fr", "CA", "xxx"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }
}

