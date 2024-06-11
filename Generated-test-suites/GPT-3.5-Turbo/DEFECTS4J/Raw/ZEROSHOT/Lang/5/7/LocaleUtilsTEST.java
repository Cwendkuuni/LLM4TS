import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test for toLocale() method
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    // Test for localeLookupList(Locale locale) method
    @Test
    public void testLocaleLookupList() {
        List<Locale> list1 = LocaleUtils.localeLookupList(new Locale("fr","CA","xxx"));
        assertEquals(new Locale("fr","CA","xxx"), list1.get(0));
        assertEquals(new Locale("fr","CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
    } 

    // Test for localeLookupList(Locale locale, Locale defaultLocale) method
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> list1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(new Locale("fr","CA","xxx"), list1.get(0));
        assertEquals(new Locale("fr","CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
        assertEquals(new Locale("en"), list1.get(3));
    } 

    // Test for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.UK));
        assertTrue(list.contains(Locale.FRANCE));
        // Add more assertions for other available locales
    }

    // Test for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.US));
        assertTrue(set.contains(Locale.UK));
        assertTrue(set.contains(Locale.FRANCE));
        // Add more assertions for other available locales
    }

    // Test for isAvailableLocale(Locale locale) method
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        // Add more assertions for other available locales
    }

    // Test for languagesByCountry(String countryCode) method
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("GB");
        assertTrue(list.contains(new Locale("en", "GB")));
        // Add more assertions for other available languages for country "GB"
    }

    // Test for countriesByLanguage(String languageCode) method
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertTrue(list.contains(new Locale("fr", "CA")));
        // Add more assertions for other available countries for language "fr"
    }

}