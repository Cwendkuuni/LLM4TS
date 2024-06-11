import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList_EmptyInput() {
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("fr", "FR"),
                new Locale("de", "DE"),
                new Locale("es", "ES")
        );
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageCode() {
        String str = "en_GB_xxx";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_Separator() {
        String str = "en_GB_xxx_";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_CountryCode() {
        String str = "en_gb";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_EmptyInput() {
        String str = "";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> actualList = LocaleUtils.languagesByCountry(null);
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    
}