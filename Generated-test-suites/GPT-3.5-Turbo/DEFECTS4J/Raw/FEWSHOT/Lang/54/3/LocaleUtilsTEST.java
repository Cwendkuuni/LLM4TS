
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("invalidLocale");
    }

    @Test
    public void testToLocaleWithNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertTrue(expected.containsAll(result));
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void testLocaleLookupListWithNullLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertTrue(expected.containsAll(result));
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
                .stream()
                .filter(locale -> locale.getCountry().equals("US") && locale.getVariant().isEmpty())
                .collect(Collectors.toList());
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
                .stream()
                .filter(locale -> locale.getLanguage().equals("en") && !locale.getCountry().isEmpty() && locale.getVariant().isEmpty())
                .collect(Collectors.toList());
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.containsAll(expected));
        assertTrue(expected.containsAll(result));
    }
}
