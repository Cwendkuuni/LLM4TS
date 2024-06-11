import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));

        // Test valid locale
        Locale localeEN = LocaleUtils.toLocale("en");
        assertEquals("en", localeEN.getLanguage());
        assertNull(localeEN.getCountry());

        Locale localeENGB = LocaleUtils.toLocale("en_GB");
        assertEquals("en", localeENGB.getLanguage());
        assertEquals("GB", localeENGB.getCountry());

        Locale localeENGBXX = LocaleUtils.toLocale("en_GB_XX");
        assertEquals("en", localeENGBXX.getLanguage());
        assertEquals("GB", localeENGBXX.getCountry());
        assertEquals("XX", localeENGBXX.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeFRCA = LocaleUtils.toLocale("fr_CA");
        Locale localeEN = LocaleUtils.toLocale("en");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(localeFRCA, localeEN);

        assertEquals(5, localeLookupList.size());
        assertEquals(localeFRCA, localeLookupList.get(0));
        assertEquals(localeFRCA, localeLookupList.get(1));
        assertEquals(localeFR_CA, localeLookupList.get(2));
        assertEquals(localeEN_CA, localeLookupList.get(3));
        assertEquals(localeEN, localeLookupList.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        int size = availableLocaleList.size();

        assertEquals(226, size);

        for (Locale locale : availableLocaleList) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();

        int sizeList = availableLocaleList.size();
        int sizeSet = availableLocaleSet.size();

        assertEquals(sizeList, sizeSet);

        for (Locale locale : availableLocaleSet) {
            assertTrue(availableLocaleList.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Locale localeFRCA = LocaleUtils.toLocale("fr_CA");
        Locale localeZH = LocaleUtils.toLocale("zh");

        assertTrue(LocaleUtils.isAvailableLocale(localeFRCA));
        assertTrue(LocaleUtils.isAvailableLocale(localeZH));
        assertFalse(LocaleUtils.isAvailableLocale(LocaleUtils.toLocale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        String countryCode = "CA";

        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(countryCode);
        int size = languagesByCountry.size();

        for (Locale locale : languagesByCountry) {
            assertEquals(countryCode, locale.getCountry());
            assertNull(locale.getVariant());
        }

        assertEquals(11, size);

        List<String> expected = Arrays.asList(
                "en",
                "fr",
                "ja",
                "ru",
                "es",
                "it",
                "de",
                "nl",
                "pt",
                "zh_CN",
                "zh_TW"
        );

        List<String> actual = new ArrayList<>();
        for (Locale locale : languagesByCountry) {
            actual.add(locale.getLanguage());
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        String languageCode = "en";

        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);
        int size = countriesByLanguage.size();

        for (Locale locale : countriesByLanguage) {
            assertEquals(languageCode, locale.getLanguage());
            assertNotNull(locale.getCountry());
            assertNull(locale.getVariant());
        }

        assertEquals(51, size);

        List<String> expected = new ArrayList<>();
        List<String> actual = new ArrayList<>();

        for (Locale locale : countriesByLanguage) {
            expected.add(locale.getCountry());
            actual.add(locale.getDisplayCountry());
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }
}