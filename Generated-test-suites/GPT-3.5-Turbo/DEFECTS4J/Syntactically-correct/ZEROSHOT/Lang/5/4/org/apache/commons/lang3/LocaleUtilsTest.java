import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Before
    public void setup() {
        // setup code here
    }

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid_format");
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.singletonList(new Locale("fr", "")), LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.singletonList(new Locale("FR", "")), LocaleUtils.countriesByLanguage("fr"));
    }

}