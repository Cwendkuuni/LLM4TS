package org.apache.commons.lang;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assert (locale.getLanguage().equals("en"));
        assert (locale.getCountry().equals(""));

        locale = LocaleUtils.toLocale("en_GB");
        assert (locale.getLanguage().equals("en"));
        assert (locale.getCountry().equals("GB"));

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assert (locale.getLanguage().equals("en"));
        assert (locale.getCountry().equals("GB"));
        assert (locale.getVariant().equals("xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assert (list.size() == 3);
        assert (list.get(0).equals(locale));
        assert (list.get(1).equals(new Locale("fr", "CA")));
        assert (list.get(2).equals(new Locale("fr")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assert (list != null);
        assert (list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assert (set != null);
        assert (set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assert (LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assert (list != null);
        assert (list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assert (list != null);
        assert (list.size() > 0);
    }

}
