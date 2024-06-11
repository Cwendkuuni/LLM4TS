import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_XX_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_XX_XX_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_XX_XX_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_XX_XX_xx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertEquals(expectedLocaleList, localeList);

        // Edge cases and exception handling tests
        locale = new Locale("fr", "CA");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        Assert.assertEquals(expectedLocaleList, localeList);

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(Collections.emptyList(), localeList);

        locale = new Locale("fr", "CA", "xxx");
        localeList = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(Collections.emptyList(), localeList);

        locale = new Locale("fr", "CA");
        localeList = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(Collections.emptyList(), localeList);

        locale = new Locale("fr");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(Collections.singletonList(new Locale("fr")), localeList);

        locale = new Locale("fr", "CA");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
        Assert.assertEquals(Collections.singletonList(new Locale("fr")), localeList);

        locale = new Locale("fr", "CA", "xxx");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en", "US", "US"));
        expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en", "US"), new Locale("en"));
        Assert.assertEquals(expectedLocaleList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(new Locale("ar"), new Locale("ca"), new Locale("cs"), new Locale("da"), new Locale("de"), new Locale("el"), new Locale("en"), new Locale("es"), new Locale("et"), new Locale("fa"), new Locale("fi"), new Locale("fr"), new Locale("he"), new Locale("hi"), new Locale("hu"), new Locale("id"), new Locale("it"), new Locale("ja"), new Locale("kn"), new Locale("ko"), new Locale("lt"), new Locale("lv"), new Locale("ms"), new Locale("nb"), new Locale("nl"), new Locale("pl"), new Locale("pt"), new Locale("ro"), new Locale("ru"), new Locale("sk"), new Locale("sl"), new Locale("sr"), new Locale("sv"), new Locale("th"), new Locale("tr"), new Locale("vi"), new Locale("zh"), new Locale("zh_CN"), new Locale("zh_HK"), new Locale("zh_TW"));
        Assert.assertEquals(expectedAvailableLocaleList, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocaleSet = Collections.unmodifiableSet(Arrays.asList(new Locale("ar"), new Locale("ca"), new Locale("cs"), new Locale("da"), new Locale("de"), new Locale("el"), new Locale("en"), new Locale("es"), new Locale("et"), new Locale("fa"), new Locale("fi"), new Locale("fr"), new Locale("he"), new Locale("hi"), new Locale("hu"), new Locale("id"), new Locale("it"), new Locale("ja"), new Locale("kn"), new Locale("ko"), new Locale("lt"), new Locale("lv"), new Locale("ms"), new Locale("nb"), new Locale("nl"), new Locale("pl"), new Locale("pt"), new Locale("ro"), new Locale("ru"), new Locale("sk"), new Locale("sl"), new Locale("sr"), new Locale("sv"), new Locale("th"), new Locale("tr"), new Locale("vi"), new Locale("zh"), new Locale("zh_CN"), new Locale("zh_HK"), new Locale("zh_TW")));
        Assert.assertEquals(expectedAvailableLocaleSet, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Edge cases and exception handling tests
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US_US")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US_US_US")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US_US_US_US")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US_US_US_US_US")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedLanguagesByCountry = Arrays.asList(new Locale("en"), new Locale("fr"));
        Assert.assertEquals(expectedLanguagesByCountry, languagesByCountry);

        // Edge cases and exception handling tests
        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), languagesByCountryNull);

        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry("");
        Assert.assertEquals(Collections.emptyList(), languagesByCountryEmpty);

        List<Locale> languagesByCountryEmptyLocale = LocaleUtils.languagesByCountry(new Locale("en", "US"));
        Assert.assertEquals(Collections.emptyList(), languagesByCountryEmptyLocale);

        List<Locale> languagesByCountryEmptyCountry = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(Collections.emptyList(), languagesByCountryEmptyCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("AU"), new Locale("NZ"));
        Assert.assertEquals(expectedCountriesByLanguage, countriesByLanguage);

        // Edge cases and exception handling tests
        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), countriesByLanguageNull);

        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage("");
        Assert.assertEquals(Collections.emptyList(), countriesByLanguageEmpty);

        List<Locale> countriesByLanguageEmptyLocale = LocaleUtils.countriesByLanguage(new Locale("en", "US"));
        Assert.assertEquals(Collections.emptyList(), countriesByLanguageEmptyLocale);

        List<Locale> countriesByLanguageEmptyLanguage = LocaleUtils.countriesByLanguage("US");
        Assert.assertEquals(Collections.emptyList(), countriesByLanguageEmptyLanguage);
    }
}