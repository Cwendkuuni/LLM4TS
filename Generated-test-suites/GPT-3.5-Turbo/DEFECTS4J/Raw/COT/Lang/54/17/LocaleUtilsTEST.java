
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test cases
        assertEquals(LocaleUtils.toLocale(null), null);
        assertEquals(LocaleUtils.toLocale("en"), new Locale("en", ""));
        assertEquals(LocaleUtils.toLocale("en_GB"), new Locale("en", "GB"));
        assertEquals(LocaleUtils.toLocale("en_GB_xxx"), new Locale("en", "GB", "xxx"));

        // Test case for JDK variant constructor changed between JDK1.3 and JDK1.4
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        String variant = locale.getVariant();
        assertTrue(variant.equals("xxx") || variant.equals("XXX"));

        // Invalid locale format test cases
        try {
            LocaleUtils.toLocale("en_");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            LocaleUtils.toLocale("en_XYZ");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            LocaleUtils.toLocale("en_GB_YYY");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(lookupList.get(0), locale);
        assertEquals(lookupList.get(1), new Locale("fr", "CA"));
        assertEquals(lookupList.get(2), new Locale("fr"));
        assertEquals(lookupList.get(3), defaultLocale);

        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(lookupList.get(0), locale);
        assertEquals(lookupList.get(1), new Locale("fr", "CA"));
        assertEquals(lookupList.get(2), new Locale("fr"));
        assertEquals(lookupList.get(3), defaultLocale);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.contains(new Locale("en")));
        assertTrue(availableLocales.contains(new Locale("fr", "CA")));
        assertTrue(availableLocales.contains(new Locale("ja")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocaleSet.contains(new Locale("en")));
        assertTrue(availableLocaleSet.contains(new Locale("fr", "CA")));
        assertTrue(availableLocaleSet.contains(new Locale("ja")));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("ja")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertTrue(languagesByCountry.contains(new Locale("en", "CA")));
        assertTrue(languagesByCountry.contains(new Locale("fr", "CA")));
        assertFalse(languagesByCountry.contains(new Locale("es", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertTrue(countriesByLanguage.contains(new Locale("en", "CA")));
        assertTrue(countriesByLanguage.contains(new Locale("en", "")));
        assertFalse(countriesByLanguage.contains(new Locale("en", "GB")));
    }

}
