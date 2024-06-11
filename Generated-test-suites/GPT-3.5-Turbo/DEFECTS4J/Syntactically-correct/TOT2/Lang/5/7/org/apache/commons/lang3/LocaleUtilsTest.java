import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertNotNull(locale1);
        assertEquals("en", locale1.getLanguage());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale2);
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale3);
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());

        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr","CA","xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));

        List<Locale> list2 = LocaleUtils.localeLookupList(new Locale("en"));
        assertNotNull(list2);
        assertEquals(4, list2.size());
        assertEquals(4, list2.size());
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
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
}