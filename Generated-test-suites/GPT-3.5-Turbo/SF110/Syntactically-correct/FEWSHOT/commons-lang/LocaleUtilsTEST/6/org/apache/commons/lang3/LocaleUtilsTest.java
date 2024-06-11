package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), Locale.Locale("", "en"));
        assertEquals(new Locale("en", "US"), Locale.Locale("en", "US"));
        assertEquals(new Locale("en", "", "variant"), Locale.Locale("en", "", "variant"));
        assertEquals(new Locale("", "US", "test"), Locale.Locale("", "US", "test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("##");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("_a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("_a_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat5() {
        LocaleUtils.toLocale("_aa_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat6() {
        LocaleUtils.toLocale("_AA_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat7() {
        LocaleUtils.toLocale("_A1_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat8() {
        LocaleUtils.toLocale("_A_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat9() {
        LocaleUtils.toLocale("_AA_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat10() {
        LocaleUtils.toLocale("_AA_1_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat11() {
        LocaleUtils.toLocale("_AA_1_test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat12() {
        LocaleUtils.toLocale("__aa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat13() {
        LocaleUtils.toLocale("__aa_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat14() {
        LocaleUtils.toLocale("__aa_BB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat15() {
        LocaleUtils.toLocale("__aa_BBB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat16() {
        LocaleUtils.toLocale("__aa_CC_fault");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), result);

        result = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Arrays.asList(Locale.US, new Locale("en")), result);

        result = LocaleUtils.localeLookupList(new Locale("en", "US", "test"));
        assertEquals(Arrays.asList(new Locale("en", "US", "test"), new Locale("en", "US"), new Locale("en")), result);

        result = LocaleUtils.localeLookupList(Locale.US, Locale.UK);
        assertEquals(Arrays.asList(Locale.US, new Locale("en"), Locale.UK), result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_LIST, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_SET, result);
    }

    @Test
    public void testIsAvailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(null);
        assertEquals(false, result);

        result = LocaleUtils.isAvailableLocale(Locale.US);
        assertEquals(true, result);

        result = LocaleUtils.isAvailableLocale(Locale.GERMANY);
        assertEquals(true, result);

        result = LocaleUtils.isAvailableLocale(new Locale("en", "US", "example"));
        assertEquals(false, result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result);

        result = LocaleUtils.languagesByCountry("AD");
        assertEquals(Collections.emptyList(), result);

        result = LocaleUtils.languagesByCountry("US");
        assertEquals(Arrays.asList(Locale.US), result);

        result = LocaleUtils.languagesByCountry("DE");
        assertEquals(Arrays.asList(Locale.GERMANY), result);

        result = LocaleUtils.languagesByCountry("IT");
        assertEquals(Arrays.asList(Locale.ITALY), result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result);

        result = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.US, Locale.UK), result);

        result = LocaleUtils.countriesByLanguage("de");
        assertEquals(Arrays.asList(Locale.GERMANY, Locale.AUSTRIA, Locale.ITALY, Locale.SWITZERLAND), result);

        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Arrays.asList(Locale.FRANCE, Locale.SWITZERLAND, Locale.BELGIUM, Locale.CANADA, Locale.LUXEMBOURG), result);
    }
}