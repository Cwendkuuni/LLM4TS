
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocaleEmptyString() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
        assertEquals("", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("#en_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat5() {
        LocaleUtils.toLocale("_us");
    }

    @Test
    public void testToLocaleLanguageCode() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
        assertEquals("", result.getVariant());
    }

    @Test
    public void testToLocaleLanguageAndCountryCode() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("", result.getVariant());
    }

    @Test
    public void testToLocaleLanguageCountryAndVariantCode() {
        Locale result = LocaleUtils.toLocale("en_US_NEWYORK");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("NEWYORK", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode1() {
        LocaleUtils.toLocale("AB_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode2() {
        LocaleUtils.toLocale("abc_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode3() {
        LocaleUtils.toLocale("a_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode1() {
        LocaleUtils.toLocale("en_ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode2() {
        LocaleUtils.toLocale("en_abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode3() {
        LocaleUtils.toLocale("en_a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode4() {
        LocaleUtils.toLocale("en_A");
    }
    
    // tests for other methods go here
    
}

