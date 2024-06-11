import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), defaultLocale), LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocales() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(Locale.CANADA), LocaleUtils.languagesByCountry("CA"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(Locale.CANADA), LocaleUtils.countriesByLanguage("en"));
    }

}