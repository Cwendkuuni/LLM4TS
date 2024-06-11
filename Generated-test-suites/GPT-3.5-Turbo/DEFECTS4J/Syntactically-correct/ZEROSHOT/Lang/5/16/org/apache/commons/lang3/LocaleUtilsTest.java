import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {

    // Test toLocale method
    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }

    // Test localeLookupList method
    @Test
    public void testLocaleLookupList() {
        Locale frCa = new Locale("fr", "CA");
        List<Locale> expectedFrCaList = Arrays.asList(frCa, new Locale("fr"), Locale.getDefault());
        assertEquals(expectedFrCaList, LocaleUtils.localeLookupList(frCa));
        
        Locale enUs = new Locale("en", "US");
        List<Locale> expectedEnUsList = Arrays.asList(enUs, new Locale("en"), Locale.getDefault());
        assertEquals(expectedEnUsList, LocaleUtils.localeLookupList(enUs));
        
        Locale zhCn = new Locale("zh", "CN");
        List<Locale> expectedZhCnList = Arrays.asList(zhCn, new Locale("zh"), Locale.getDefault());
        assertEquals(expectedZhCnList, LocaleUtils.localeLookupList(zhCn));
        
        List<Locale> expectedNullList = Collections.emptyList();
        assertEquals(expectedNullList, LocaleUtils.localeLookupList(null));
    }

    // Test availableLocaleList method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, LocaleUtils.availableLocaleList());
    }

    // Test availableLocaleSet method
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(new HashSet<>(expectedLocaleList), LocaleUtils.availableLocaleSet());
    }

    // Test isAvailableLocale method
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    // Test languagesByCountry method
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedFr = Arrays.asList(Locale.FRENCH, new Locale("fr_CA"), new Locale("fr_CH"));
        assertEquals(expectedFr, LocaleUtils.languagesByCountry("FR"));
        
        List<Locale> expectedNull = Collections.emptyList();
        assertEquals(expectedNull, LocaleUtils.languagesByCountry(null));
    }

    // Test countriesByLanguage method
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedEn = Arrays.asList(Locale.US, Locale.UK, new Locale("en_CA"));
        assertEquals(expectedEn, LocaleUtils.countriesByLanguage("en"));
        
        List<Locale> expectedNull = Collections.emptyList();
        assertEquals(expectedNull, LocaleUtils.countriesByLanguage(null));
    }

}