import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        assertEquals(Arrays.asList(new Locale("fr","CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en")));
        assertEquals(Arrays.asList(new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr"), new Locale("en")));
        assertEquals(Arrays.asList(new Locale("en")), LocaleUtils.localeLookupList(new Locale("en"), new Locale("en")));
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales.size(), Locale.getAvailableLocales().length);
        assertEquals(new HashSet<>(availableLocales), new HashSet<>(Arrays.asList(Locale.getAvailableLocales())));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocales.size(), Locale.getAvailableLocales().length);
        assertEquals(availableLocales, new HashSet<>(Arrays.asList(Locale.getAvailableLocales())));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(new Locale("fr","CA","xxx"), new Locale("fr","CA")), LocaleUtils.languagesByCountry("CA"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("ZZ"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(new Locale("fr","CA"), new Locale("fr")), LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("ZZ"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}