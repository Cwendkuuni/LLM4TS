import org.junit.*;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {
    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocaleEmpty() {
        Locale locale = LocaleUtils.toLocale("");
        assertNotNull(locale);
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("#");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("_a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat5() {
        LocaleUtils.toLocale("__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat6() {
        LocaleUtils.toLocale("a_a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat7() {
        LocaleUtils.toLocale("aa_a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat8() {
        LocaleUtils.toLocale("aa_aA");
    }
    
    @Test
    public void testToLocaleValidFormat1() {
        Locale locale = LocaleUtils.toLocale("aa");
        assertNotNull(locale);
        assertEquals("aa", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocaleValidFormat2() {
        Locale locale = LocaleUtils.toLocale("aa_AA");
        assertNotNull(locale);
        assertEquals("aa", locale.getLanguage());
        assertEquals("AA", locale.getCountry());
    }
    
    @Test
    public void testToLocaleValidFormat3() {
        Locale locale = LocaleUtils.toLocale("aa_AA_XX");
        assertNotNull(locale);
        assertEquals("aa", locale.getLanguage());
        assertEquals("AA", locale.getCountry());
        assertEquals("XX", locale.getVariant());
    }
    
    @Test
    public void testLocaleLookupListNull() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "POSIX");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("en", "US")));
        assertTrue(list.contains(new Locale("en", "")));
        assertTrue(list.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), list);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertEquals(new HashSet<Locale>(list), LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocaleFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    @Test
    public void testIsAvailableLocaleTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testLanguagesByCountryNull() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertTrue(list.contains(Locale.US));
    }
    
    @Test
    public void testCountriesByLanguageNull() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertTrue(list.contains(Locale.US));
    }
}