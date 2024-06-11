import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Locale;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "NY"), LocaleUtils.toLocale("en_US_NY"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "US", "NY"), LocaleUtils.toLocale("_US_NY"));
        assertEquals(new Locale("", "", "NY"), LocaleUtils.toLocale("_NY"));
        assertEquals(new Locale("", "", "NY"), LocaleUtils.toLocale("__NY"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("zh", "CN");
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        assertEquals(new Locale("en"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        assertEquals(new Locale("en", ""), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
        assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_LIST, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertEquals(true, LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}