import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enGB");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));

        Locale locale2 = new Locale("en");
        Locale defaultLocale = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(4, list2.size());
        assertEquals(locale2, list2.get(0));
        assertEquals(new Locale("en"), list2.get(1));
        assertEquals(new Locale("en", ""), list2.get(2));
        assertEquals(defaultLocale, list2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("invalid", "locale");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list1 = LocaleUtils.languagesByCountry("US");
        assertTrue(list1.contains(new Locale("en", "US")));
        assertTrue(list1.contains(new Locale("es", "US")));

        List<Locale> list2 = LocaleUtils.languagesByCountry("ZZ");
        assertEquals(0, list2.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertTrue(list1.contains(new Locale("en", "US")));
        assertTrue(list1.contains(new Locale("en", "GB")));

        List<Locale> list2 = LocaleUtils.countriesByLanguage("zz");
        assertEquals(0, list2.size());
    }

}