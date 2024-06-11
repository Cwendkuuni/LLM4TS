package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String input;
    private Locale expected;

    public LocaleUtilsTest(String input, Locale expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"en", new Locale("en")},
            {"en_GB", new Locale("en", "GB")},
            {"en_GB_London", new Locale("en", "GB", "London")},
            {"fr", new Locale("fr")},
            {"fr_CA", new Locale("fr", "CA")},
            {"fr_CA_Toronto", new Locale("fr", "CA", "Toronto")},
            {null, null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = LocaleUtils.toLocale("fr_CA_Toronto");
        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> expectedList = LocaleUtils.localeLookupList(baseLocale, Locale.getDefault());

        for (int i = 0; i < localeList.size(); i++) {
            Locale locale = localeList.get(i);
            Locale expectedLocale = expectedList.get(i);
            assertEquals(expectedLocale.getLanguage(), locale.getLanguage());
            assertEquals(expectedLocale.getCountry(), locale.getCountry());
            assertEquals(expectedLocale.getVariant(), locale.getVariant());
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());

        for (int i = 0; i < actual.size(); i++) {
            Locale actualLocale = actual.get(i);
            Locale expectedLocale = expected.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(Locale.CANADA, new Locale("fr", "CA"), new Locale("en", "CA"));

        for (int i = 0; i < actual.size(); i++) {
            Locale actualLocale = actual.get(i);
            Locale expectedLocale = expected.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(Locale.US, new Locale("en", "CA"), new Locale("en", "GB"));

        for (int i = 0; i < actual.size(); i++) {
            Locale actualLocale = actual.get(i);
            Locale expectedLocale = expected.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
        }
    }

}