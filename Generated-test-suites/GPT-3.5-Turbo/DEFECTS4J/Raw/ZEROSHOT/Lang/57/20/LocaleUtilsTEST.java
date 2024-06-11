import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    private LocaleUtils localeUtils;

    @Before
    public void setUp() {
        localeUtils = new LocaleUtils();
    }

    @Test
    public void testToLocale() {
        Locale result1 = localeUtils.toLocale("en");
        assertEquals(new Locale("en", ""), result1);

        Locale result2 = localeUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), result2);

        Locale result3 = localeUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), result3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        localeUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> result1 = localeUtils.localeLookupList(locale1);
        List<Locale> expected1 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected1, result1);

        Locale locale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result2 = localeUtils.localeLookupList(locale2, defaultLocale);
        List<Locale> expected2 = Arrays.asList(locale2, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expected2, result2);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = localeUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = localeUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(localeUtils.availableLocaleList());
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = Locale.US;
        assertTrue(localeUtils.isAvailableLocale(locale));

        Locale invalidLocale = new Locale("invalid");
        assertFalse(localeUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> result1 = localeUtils.languagesByCountry(countryCode);
        List<Locale> expected1 = new ArrayList<>();
        for (Locale availableLocale : localeUtils.availableLocaleList()) {
            if (countryCode.equals(availableLocale.getCountry())
                    && availableLocale.getVariant().equals("")) {
                expected1.add(availableLocale);
            }
        }
        assertEquals(expected1, result1);

        String invalidCountryCode = "XX";
        List<Locale> result2 = localeUtils.languagesByCountry(invalidCountryCode);
        List<Locale> expected2 = Collections.emptyList();
        assertEquals(expected2, result2);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result1 = localeUtils.countriesByLanguage(languageCode);
        List<Locale> expected1 = new ArrayList<>();
        for (Locale availableLocale : localeUtils.availableLocaleList()) {
            if (languageCode.equals(availableLocale.getLanguage())
                    && !availableLocale.getCountry().equals("")
                    && availableLocale.getVariant().equals("")) {
                expected1.add(availableLocale);
            }
        }
        assertEquals(expected1, result1);

        String invalidLanguageCode = "xx";
        List<Locale> result2 = localeUtils.countriesByLanguage(invalidLanguageCode);
        List<Locale> expected2 = Collections.emptyList();
        assertEquals(expected2, result2);
    }
}