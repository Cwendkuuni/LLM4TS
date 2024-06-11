
import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), 
                     LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), 
                     LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH), LocaleUtils.languagesByCountry("CA"));
    }
    
    @Test
    public void testLanguagesByCountryNoMatchingCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XY"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(Locale.CANADA, Locale.CHINA), LocaleUtils.countriesByLanguage("zh"));
    }
    
    @Test
    public void testCountriesByLanguageNoMatchingLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xy"));
    }
    
}
