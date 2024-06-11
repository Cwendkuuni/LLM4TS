import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NormalCase() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);
    }

    @Test
    public void testToLocale_CountryCase() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }

    @Test
    public void testToLocale_VariantCase() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_SpecificLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertTrue(list.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr")));
    }

    @Test
    public void testLocaleLookupList_GenericLocale() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("de");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertTrue(list.contains(new Locale("en")));
        assertTrue(list.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertTrue(list.size() > 0);
    }
}