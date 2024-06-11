
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(Locale.ENGLISH, locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());

        Locale locale4 = LocaleUtils.toLocale(null);
        assertNull(locale4);

        try {
            Locale locale5 = LocaleUtils.toLocale("invalid");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(4, lookupList1.size());
        assertTrue(lookupList1.contains(locale1));
        assertTrue(lookupList1.contains(new Locale("fr", "CA")));
        assertTrue(lookupList1.contains(new Locale("fr")));
        assertTrue(lookupList1.contains(Locale.getDefault()));

        Locale locale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(4, lookupList2.size());
        assertTrue(lookupList2.contains(locale2));
        assertTrue(lookupList2.contains(new Locale("fr", "CA")));
        assertTrue(lookupList2.contains(new Locale("fr")));
        assertTrue(lookupList2.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertNotNull(localeList);
        assertTrue(localeList.contains(Locale.ENGLISH));
        assertTrue(localeList.contains(Locale.CANADA));
        assertTrue(localeList.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet);
        assertTrue(localeSet.contains(Locale.ENGLISH));
        assertTrue(localeSet.contains(Locale.CANADA));
        assertTrue(localeSet.contains(Locale.getDefault()));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages1 = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages1);
        assertEquals(2, languages1.size());
        assertTrue(languages1.contains(new Locale("en", "CA")));
        assertTrue(languages1.contains(new Locale("fr", "CA")));

        List<Locale> languages2 = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages2);
        assertTrue(languages2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries1);
        assertEquals(3, countries1.size());
        assertTrue(countries1.contains(new Locale("en", "AU")));
        assertTrue(countries1.contains(new Locale("en", "CA")));
        assertTrue(countries1.contains(new Locale("en", "GB")));

        List<Locale> countries2 = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries2);
        assertTrue(countries2.isEmpty());
    }

}

