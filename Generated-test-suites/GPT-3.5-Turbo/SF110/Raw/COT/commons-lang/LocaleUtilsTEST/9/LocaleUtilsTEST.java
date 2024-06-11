
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyStringInput_ReturnEmptyLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan2Chars_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test
    public void testToLocale_LanguageOnly_ReturnLocaleWithLanguage() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCodeWithMoreThan3Chars_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("eng");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCodeWithLowercaseChars_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("En");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCodeWithUppercaseChars_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN");
    }

    @Test
    public void testToLocale_LanguageAndCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test
    public void testToLocale_LanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNoVariant_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }

    // Add more test cases for other methods in LocaleUtils

}

