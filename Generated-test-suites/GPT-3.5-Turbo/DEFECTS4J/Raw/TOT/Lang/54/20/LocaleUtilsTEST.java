import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(expectedLocale1, actualLocale1);

        // Edge case
        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expectedLocale2, actualLocale2);

        // Error scenario
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Null input
        Locale expectedLocale3 = null;
        Locale actualLocale3 = LocaleUtils.toLocale(null);
        Assert.assertEquals(expectedLocale3, actualLocale3);
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale inputLocale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList1 = new ArrayList<>(Arrays.asList(new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"), new Locale("fr")));
        List<Locale> actualList1 = LocaleUtils.localeLookupList(inputLocale1);
        Assert.assertEquals(expectedList1, actualList1);

        // Edge case
        Locale inputLocale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale2 = new Locale("en");
        List<Locale> expectedList2 = new ArrayList<>(Arrays.asList(new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"), new Locale("fr"), new Locale("en")));
        List<Locale> actualList2 = LocaleUtils.localeLookupList(inputLocale2, defaultLocale2);
        Assert.assertEquals(expectedList2, actualList2);

        // Null input
        List<Locale> expectedList3 = Collections.emptyList();
        List<Locale> actualList3 = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expectedList3, actualList3);
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Locale locale1 = new Locale("en", "");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Edge case
        Locale locale2 = new Locale("zh", "CN");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale2));

        // Error scenario
        Locale locale3 = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale3));

        // Null input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(new Locale("en", "CA")));
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expectedList, actualList);

        // Edge case
        List<Locale> expectedList2 = Collections.emptyList();
        List<Locale> actualList2 = LocaleUtils.languagesByCountry("XX");
        Assert.assertEquals(expectedList2, actualList2);

        // Null input
        List<Locale> expectedList3 = Collections.emptyList();
        List<Locale> actualList3 = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expectedList3, actualList3);
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(new Locale("en", "GB")));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedList, actualList);

        // Edge case
        List<Locale> expectedList2 = Collections.emptyList();
        List<Locale> actualList2 = LocaleUtils.countriesByLanguage("xx");
        Assert.assertEquals(expectedList2, actualList2);

        // Null input
        List<Locale> expectedList3 = Collections.emptyList();
        List<Locale> actualList3 = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expectedList3, actualList3);
    }

}