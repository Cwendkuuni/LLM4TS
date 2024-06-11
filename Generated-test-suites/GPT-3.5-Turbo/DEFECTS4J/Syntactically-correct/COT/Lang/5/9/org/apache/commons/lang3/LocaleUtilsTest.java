import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("ENG");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleInvalidLength() {
        LocaleUtils.toLocale("en_GB_US");
    }

    @Test
    public void testLocaleLookupListNonNullLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(Locale.getDefault());
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        List<Locale> expectedList = new ArrayList<Locale>();
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale != null) {
            expectedList.add(defaultLocale);
            expectedList.add(new Locale(defaultLocale.getLanguage(), ""));
        }
        assertEquals(expectedList, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupListNonNullAndNullLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testLocaleLookupListNullAndNonNullLocale() {
        Locale defaultLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(defaultLocale);
        expectedList.add(new Locale(defaultLocale.getLanguage(), ""));
        assertEquals(expectedList, LocaleUtils.localeLookupList(null, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("aa", "")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("en", "AU"));
        expectedList.add(new Locale("en", "CA"));
        expectedList.add(new Locale("en", "IE"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("GB"));
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("fr", "CH"));
        expectedList.add(new Locale("fr", "BE"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}