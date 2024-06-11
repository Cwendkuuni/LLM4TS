package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String localeString;
    private Locale expectedLocale;

    public LocaleUtilsTest(String localeString, Locale expectedLocale) {
        this.localeString = localeString;
        this.expectedLocale = expectedLocale;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"en", new Locale("en", "")},
            {"en_GB", new Locale("en", "GB")},
            {"en_GB_xxx", new Locale("en", "GB", "xxx")},
            {"fr", new Locale("fr", "")},
            {"fr_CA", new Locale("fr", "CA")},
            {"fr_CA_xxx", new Locale("fr", "CA", "xxx")},
            {"fr_CH", new Locale("fr", "CH")},
            {"es", new Locale("es", "")},
            {"es_ES", new Locale("es", "ES")},
            {"es_ES_xxx", new Locale("es", "ES", "xxx")},
            {"es_MX", new Locale("es", "MX")},
            {"es_MX_xxx", new Locale("es", "MX", "xxx")},
            {null, null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);
    }

    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        String invalidLocaleString = "InvalidFormat";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    //-----------------------------------------------------------------------

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(6, localeList.size());
        assertEquals(baseLocale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(new Locale("en"), localeList.get(5));

        LocaleUtils.localeLookupList(baseLocale); // no defaultLocale -> empty list
        LocaleUtils.localeLookupList(defaultLocale); // no baseLocale -> empty list
    }

    //-----------------------------------------------------------------------

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(227, availableLocaleList.size());
        assertTrue(availableLocaleList.contains(new Locale("en", "US")));
        assertTrue(availableLocaleList.contains(new Locale("fr", "FR")));
        assertTrue(availableLocaleList.contains(new Locale("es", "ES")));
        assertFalse(availableLocaleList.contains(new Locale("xyz", "xyz")));
    }

    //-----------------------------------------------------------------------

    @Test
    public void testLanguagesByCountry() {
        assertEquals(1, LocaleUtils.languagesByCountry("US").size());
        assertEquals(Locale.LanguageRange.FR, Locale.LanguageRange.parse("fr"));
        assertEquals(Locale.LanguageRange.parse("fr,es"), Locale.LanguageRange.parse(LocaleUtils.languagesByCountry("ES")
                .get(0).getDisplayLanguage()));
        assertEquals(Locale.LanguageRange.parse("es"), Locale.LanguageRange.parse(LocaleUtils.languagesByCountry("ES")
                .get(1).getDisplayLanguage()));
    }

    //-----------------------------------------------------------------------

    @Test
    public void testCountriesByLanguage() {
        assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
        assertEquals(Locale.CountryRange.CA, Locale.CountryRange.parse("CA"));
        assertEquals(Locale.CountryRange.parse("MX,US"), Locale.CountryRange.parse(LocaleUtils.countriesByLanguage("ES")
                .get(0).getCountry()));
        assertEquals(Locale.CountryRange.parse("MX"), Locale.CountryRange.parse(LocaleUtils.countriesByLanguage("ES")
                .get(1).getCountry()));
    }

}