import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test null input
        assertEquals(null, LocaleUtils.toLocale(null));

        // Test valid inputs
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid inputs
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException to be thrown, but it wasn't");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: ", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException to be thrown, but it wasn't");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
        // ...
    }

    @Test
    public void testLocaleLookupList() {
        // Test null input
        List<Locale> expectedList1 = new ArrayList<>();
        List<Locale> resultList1 = LocaleUtils.localeLookupList(null);
        assertEquals(expectedList1, resultList1);

        // Test valid inputs
        Locale locale2 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("fr", "CA", "xxx"));
        expectedList2.add(new Locale("fr", "CA"));
        expectedList2.add(new Locale("fr"));
        List<Locale> resultList2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(expectedList2, resultList2);

        // Test valid inputs with default locale
        Locale locale3 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale3 = new Locale("en");
        List<Locale> expectedList3 = new ArrayList<>();
        expectedList3.add(new Locale("fr", "CA", "xxx"));
        expectedList3.add(new Locale("fr", "CA"));
        expectedList3.add(new Locale("fr"));
        expectedList3.add(new Locale("en"));
        List<Locale> resultList3 = LocaleUtils.localeLookupList(locale3, defaultLocale3);
        assertEquals(expectedList3, resultList3);

        // ...
    }

    @Test
    public void testAvailableLocaleList() {
        // Test the size of the available locale list
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> resultList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList.size(), resultList.size());

        // Test the content of the available locale list
        for (Locale locale : expectedList) {
            assertTrue(resultList.contains(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test the size of the available locale set
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet.size(), resultSet.size());

        // Test the content of the available locale set
        for (Locale locale : expectedSet) {
            assertTrue(resultSet.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test valid inputs
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));

        // Test invalid inputs
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        // ...
    }

    @Test
    public void testLanguagesByCountry() {
        // Test null input
        List<Locale> expectedList1 = new ArrayList<>();
        List<Locale> resultList1 = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedList1, resultList1);

        // Test valid inputs
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("en", ""));
        List<Locale> resultList2 = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedList2, resultList2);

        // Test invalid inputs
        List<Locale> expectedList3 = new ArrayList<>();
        List<Locale> resultList3 = LocaleUtils.languagesByCountry("XX");
        assertEquals(expectedList3, resultList3);
        // ...
    }

    @Test
    public void testCountriesByLanguage() {
        // Test null input
        List<Locale> expectedList1 = new ArrayList<>();
        List<Locale> resultList1 = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedList1, resultList1);

        // Test valid inputs
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("US"));
        List<Locale> resultList2 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList2, resultList2);

        // Test invalid inputs
        List<Locale> expectedList3 = new ArrayList<>();
        List<Locale> resultList3 = LocaleUtils.countriesByLanguage("xx");
        assertEquals(expectedList3, resultList3);
        // ...
    }
}