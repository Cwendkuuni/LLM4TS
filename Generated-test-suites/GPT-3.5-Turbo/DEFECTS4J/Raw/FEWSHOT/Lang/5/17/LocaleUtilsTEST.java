import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        trueExceptionThrownForInvalidLocaleFormat("", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("e", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("EN", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("En_GB", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("en_gb", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("en__GB", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("en_GB_x", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("en_GB_", IllegalArgumentException.class);
        trueExceptionThrownForInvalidLocaleFormat("en_GB_xx", IllegalArgumentException.class);
    }
    
    @Test
    public void testLocaleLookupList() {
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        
        List<Locale> expectedList1 = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedList1, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        
        List<Locale> expectedList2 = Arrays.asList(new Locale("fr"), new Locale("en"));
        assertEquals(expectedList2, LocaleUtils.localeLookupList(null, new Locale("en")));
        
        List<Locale> expectedList3 = Collections.singletonList(new Locale("en"));
        assertEquals(expectedList3, LocaleUtils.localeLookupList(null, new Locale("en")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.UK));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.UK));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("ZZ"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Collections.singletonList(new Locale("fr", "CA"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("ZZ"));
    }
    
    private void trueExceptionThrownForInvalidLocaleFormat(String str, Class<? extends Exception> exceptionClass) {
        try {
            LocaleUtils.toLocale(str);
        } catch (Exception e) {
            assertTrue(e.getClass().equals(exceptionClass));
        }
    }
}