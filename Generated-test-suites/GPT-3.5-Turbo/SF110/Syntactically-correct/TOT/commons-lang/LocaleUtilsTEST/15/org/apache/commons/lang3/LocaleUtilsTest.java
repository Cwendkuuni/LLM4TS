import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {
    
    private static Locale frenchLocale;
    private static Locale emptyLocale;
    private static Locale underscoreLocale;
    private static Locale threeCharacterLocale;
    private static Locale twoCharacterLocale;
  
    @BeforeClass
    public static void setup() {
        frenchLocale = new Locale("fr", "FR");
        emptyLocale = new Locale("", "");
        underscoreLocale = new Locale("_");
        threeCharacterLocale = new Locale("__");
        twoCharacterLocale = new Locale("EN");
    }
    
    // Test for toLocale() method
    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString() {
        assertEquals(emptyLocale, LocaleUtils.toLocale(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("#abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_LanguageTwoCharacters() {
        assertEquals(twoCharacterLocale, LocaleUtils.toLocale("EN"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LanguageTwoCharactersUpperCase() {
        LocaleUtils.toLocale("EN_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithUnderscore() {
        LocaleUtils.toLocale("_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithThreeUnderscores() {
        LocaleUtils.toLocale("__");
    }
    
    // Test for localeLookupList() method
    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale() {
        List<Locale> expectedList = Arrays.asList(frenchLocale, new Locale("fr", ""), emptyLocale, frenchLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(frenchLocale, frenchLocale));
    }
    
    // Test for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }
    
    // Test for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }
    
    // Test for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale_ValidLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(frenchLocale));
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("__")));
    }
    
    // Test for languagesByCountry() method
    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry("AB"));
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> expectedList = Arrays.asList(frenchLocale);
        assertEquals(expectedList, LocaleUtils.languagesByCountry("FR"));
    }
    
    // Test for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("ZZ"));
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> expectedList = Arrays.asList(frenchLocale);
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }
}