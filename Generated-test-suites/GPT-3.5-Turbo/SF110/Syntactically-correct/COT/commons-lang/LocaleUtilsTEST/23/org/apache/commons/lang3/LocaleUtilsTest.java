
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertEquals(null, LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormatString_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("#");
        });
    }

    @Test
    public void testToLocale_InvalidFormatStringWithLengthLessThan2_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("a");
        });
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale_ReturnsListWithPassedInLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        assertEquals(Collections.singletonList(locale), result);
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

    @Test
    public void testAvailableLocaleList_ReturnsNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result);
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result);
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

}
