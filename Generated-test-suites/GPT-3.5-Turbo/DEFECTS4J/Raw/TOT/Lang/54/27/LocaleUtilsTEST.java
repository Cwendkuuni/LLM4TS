import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    private static final String VALID_LOCALE = "en_GB";
    private static final String INVALID_LOCALE = "invalid_locale";

    private Locale validLocale;
    private Locale invalidLocale;

    @Before
    public void setUp() {
        validLocale = new Locale("en", "GB");
        invalidLocale = null;
    }

    @Test
    public void toLocale_ValidString_ReturnsLocaleObject() {
        assertEquals(validLocale, LocaleUtils.toLocale(VALID_LOCALE));
    }

    @Test
    public void toLocale_InvalidString_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale(INVALID_LOCALE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void toLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void localeLookupList_NullLocale_ReturnsEmptyList() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void localeLookupList_ValidLocale_ReturnsCorrectList() {
        assertEquals(4, LocaleUtils.localeLookupList(validLocale).size());
        assertEquals(validLocale, LocaleUtils.localeLookupList(validLocale).get(0));
        assertEquals(Locale.ENGLISH, LocaleUtils.localeLookupList(validLocale).get(3));
    }

    @Test
    public void localeLookupList_ValidLocaleWithDefaultLocale_ReturnsCorrectList() {
        Locale defaultLocale = Locale.CANADA;
        assertEquals(4, LocaleUtils.localeLookupList(validLocale, defaultLocale).size());
        assertEquals(validLocale, LocaleUtils.localeLookupList(validLocale, defaultLocale).get(0));
        assertEquals(defaultLocale, LocaleUtils.localeLookupList(validLocale, defaultLocale).get(3));
    }

    @Test
    public void availableLocaleList_ReturnsUnmodifiableList() {
        try {
            LocaleUtils.availableLocaleList().add(validLocale);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void availableLocaleSet_ReturnsUnmodifiableSet() {
        try {
            LocaleUtils.availableLocaleSet().add(validLocale);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void isAvailableLocale_ValidLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
    }

    @Test
    public void isAvailableLocale_InvalidLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void languagesByCountry_ValidCountryCode_ReturnsUnmodifiableList() {
        try {
            LocaleUtils.languagesByCountry("GB").add(validLocale);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void countriesByLanguage_ValidLanguageCode_ReturnsUnmodifiableList() {
        try {
            LocaleUtils.countriesByLanguage("en").add(validLocale);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
}