import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_gb");
    }
    
    @Test
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }
    
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> localeList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(new HashSet<>(localeList), LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en")));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("foo")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(
            new Locale("en", "US"),
            new Locale("es", "US")
        );
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }
    
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(
            new Locale("US"),
            new Locale("GB")
        );
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
    
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }

}