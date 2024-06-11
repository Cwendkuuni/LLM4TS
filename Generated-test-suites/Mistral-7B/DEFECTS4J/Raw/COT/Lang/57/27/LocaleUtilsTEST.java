import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] validLocales = {"en", "en_GB", "en_GB_xxx", "fr_CA", "fr_CA_xxx", "fr_FR"};
        for (String localeString : validLocales) {
            Locale expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3));
            if (localeString.length() > 5) {
                expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3), localeString.substring(6));
            }
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals("Invalid locale conversion from string: " + localeString, expectedLocale, actualLocale);
        }

        // Test invalid locale
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ab_cd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals("Invalid locale lookup list for locale [" + locale + "]", expectedList, actualList);

        locale = new Locale("en");
        expectedList = Arrays.asList(new Locale("en"));
        actualList = LocaleUtils.localeLookupList(locale, null);
        assertEquals("Invalid locale lookup list for locale [" + locale + "]", expectedList, actualList);

        locale = new Locale("fr", "CA");
        expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals("Invalid locale lookup list for locale [" + locale + "]", expectedList, actualList);

        // Test empty list
        Locale emptyLocale = null;
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.localeLookupList(emptyLocale, new Locale("en"));
        assertEquals("Invalid locale lookup list for locale [" + emptyLocale + "]", expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Locale.getAvailableLocales();
        assertEquals("Invalid available locale list", expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        Locale locale3 = new Locale("en", "CA");
        assertTrue("Locale [" + locale1 + "] should be available", LocaleUtils.isAvailableLocale(locale1));
        assertTrue("Locale [" + locale2 + "] should be available", LocaleUtils.isAvailableLocale(locale2));
        assertTrue("Locale [" + locale3 + "] should be available", LocaleUtils.isAvailableLocale(locale3));

        Locale locale4 = new Locale("ab", "CD");
        assertFalse("Locale [" + locale4 + "] should not be available", LocaleUtils.isAvailableLocale(locale4));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList1 = Arrays.asList(new Locale("fr"), new Locale("en"));
        List<Locale> expectedList2 = Arrays.asList(new Locale("fr"), new Locale("en"), new Locale("de"));
        List<Locale> actualList1 = LocaleUtils.languagesByCountry("CA");
        List<Locale> actualList2 = LocaleUtils.languagesByCountry("DE");
        assertEquals("Invalid languages by country list for country [CA]", expectedList1, actualList1);
        assertEquals("Invalid languages by country list for country [DE]", expectedList2, actualList2);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList1 = Arrays.asList(new Locale("CA"), new Locale("US"));
        List<Locale> expectedList2 = Arrays.asList(new Locale("DE"), new Locale("AT"), new Locale("CH"));
        List<Locale> actualList1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> actualList2 = LocaleUtils.countriesByLanguage("de");
        assertEquals("Invalid countries by language list for language [en]", expectedList1, actualList1);
        assertEquals("Invalid countries by language list for language [de]", expectedList2, actualList2);
    }
}