import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_xxx";
        Locale expected3 = new Locale("en", "GB", "xxx");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);

        // Test invalid input
        String input4 = null;
        Locale actual4 = LocaleUtils.toLocale(input4);
        assertNull(actual4);

        String input5 = "";
        Locale actual5 = LocaleUtils.toLocale(input5);
        assertNull(actual5);

        String input6 = "InvalidLocaleFormat";
        try {
            Locale actual6 = LocaleUtils.toLocale(input6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        String input7 = "fr_CA_XXX";
        Locale actual7 = LocaleUtils.toLocale(input7);
        assertEquals(new Locale("fr", "CA"), actual7);

        String input8 = "fr_CA_XXX_YYY";
        Locale actual8 = LocaleUtils.toLocale(input8);
        assertEquals(new Locale("fr", "CA"), actual8);

        String input9 = "fr_XXX";
        Locale actual9 = LocaleUtils.toLocale(input9);
        assertEquals(new Locale("fr"), actual9);

        String input10 = "fr_XXX_YYY";
        Locale actual10 = LocaleUtils.toLocale(input10);
        assertEquals(new Locale("fr"), actual10);
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale input1 = null;
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.localeLookupList(input1);
        assertEquals(expected1, actual1);

        // Test valid input with no default locale
        Locale input2 = new Locale("fr", "CA");
        List<Locale> expected2 = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual2 = LocaleUtils.localeLookupList(input2);
        assertEquals(expected2, actual2);

        // Test valid input with default locale
        Locale input3 = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected3 = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"), defaultLocale);
        List<Locale> actual3 = LocaleUtils.localeLookupList(input3, defaultLocale);
        assertEquals(expected3, actual3);

        // Test invalid input
        Object input4 = new Object();
        List<Locale> actual4 = LocaleUtils.localeLookupList(input4);
        assertEquals(Collections.emptyList(), actual4);

        Locale input5 = null;
        List<Locale> actual5 = LocaleUtils.localeLookupList(input5);
        assertEquals(Collections.emptyList(), actual5);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> actual1 = LocaleUtils.availableLocaleList();
        assertEquals(Collections.emptyList(), actual1);

        // Test valid list
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(Locale.ENGLISH);
        expected1.add(Locale.FRENCH);
        List<Locale> actual1_expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        LocaleUtils.cAvailableLocaleList = new ArrayList<>();
        LocaleUtils.cAvailableLocaleList.addAll(Locale.getAvailableLocales());
        List<Locale> actual1_actual = LocaleUtils.availableLocaleList();
        assertEquals(actual1_expected, actual1_actual);
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = null;
        List<Locale> actual1 = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), actual1);

        // Test valid list
        String countryCode1 = "CA";
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(new Locale("en", "CA"));
        expected1.add(new Locale("fr", "CA"));
        List<Locale> actual1_expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        LocaleUtils.cLanguagesByCountry.put(countryCode1, null);
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getCountry().equals(countryCode1)) {
                LocaleUtils.cLanguagesByCountry.put(countryCode1, new ArrayList<>(Arrays.asList(locale)));
                break;
            }
        }
        List<Locale> actual1_actual = LocaleUtils.languagesByCountry(countryCode1);
        assertEquals(expected1, actual1_actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        String languageCode = null;
        List<Locale> actual1 = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(Collections.emptyList(), actual1);

        // Test valid list
        String languageCode1 = "en";
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(new Locale("CA", "en"));
        expected1.add(new Locale("GB", "en"));
        List<Locale> actual1_expected = Arrays.asList(new Locale("CA", "en"), new Locale("GB", "en"));
        LocaleUtils.cCountriesByLanguage.put(languageCode1, null);
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equals(languageCode1)) {
                LocaleUtils.cCountriesByLanguage.put(languageCode1, new ArrayList<>(Arrays.asList(locale)));
                break;
            }
        }
        List<Locale> actual1_actual = LocaleUtils.countriesByLanguage(languageCode1);
        assertEquals(expected1, actual1_actual);
    }

}