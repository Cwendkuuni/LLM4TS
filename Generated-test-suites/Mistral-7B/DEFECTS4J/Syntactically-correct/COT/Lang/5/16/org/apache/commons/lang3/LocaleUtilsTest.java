import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Test for null input
        String nullLocaleString = null;
        Locale nullLocale = LocaleUtils.toLocale(nullLocaleString);
        assertNull(nullLocale);

        // Test for invalid format
        String invalidLocaleString = "xx_XX_xx";
        Locale invalidLocale = LocaleUtils.toLocale(invalidLocaleString);
        assertNull(invalidLocale);

        // Test for empty locale string
        String emptyLocaleString = "";
        Locale emptyLocale = LocaleUtils.toLocale(emptyLocaleString);
        assertNull(emptyLocale);
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocales = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocales = new ArrayList<>();
        actualLocales.add(baseLocale);
        actualLocales.add(new Locale("fr", "CA"));
        actualLocales.add(new Locale("fr"));
        assertEquals(expectedLocales, actualLocales);

        // Test for null input
        Locale nullLocale = null;
        List<Locale> nullLocales = LocaleUtils.localeLookupList(nullLocale);
        assertEquals(Collections.emptyList(), nullLocales);

        // Test for empty list
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(emptyList, emptyLocaleList);

        // Test for default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(defaultLocale);
        List<Locale> expectedLocaleLookupList = new ArrayList<>();
        expectedLocaleLookupList.add(defaultLocale);
        assertEquals(expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);

        // Test for null input
        Locale nullLocale = null;
        List<Locale> nullLocales = LocaleUtils.availableLocaleList();
        assertEquals(Collections.emptyList(), nullLocales);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        String invalidLocaleString = "xx_XX";
        LocaleUtils.toLocale(invalidLocaleString);
    }
}