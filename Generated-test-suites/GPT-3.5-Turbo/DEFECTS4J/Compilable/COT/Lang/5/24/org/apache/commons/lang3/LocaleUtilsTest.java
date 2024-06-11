package org.apache.commons.lang3;

import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");

        assert locale1.getLanguage().equals("en");
        assert locale1.getCountry().equals("");
        assert locale2.getLanguage().equals("en");
        assert locale2.getCountry().equals("GB");
        assert locale3.getLanguage().equals("en");
        assert locale3.getCountry().equals("GB");
        assert locale3.getVariant().equals("xxx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);

        assert localeLookupList.equals(expectedList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();

        assert availableLocaleList.contains(Locale.ENGLISH);
        assert availableLocaleList.contains(Locale.FRENCH);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();

        assert availableLocaleSet.contains(Locale.ENGLISH);
        assert availableLocaleSet.contains(Locale.FRENCH);
    }

    @Test
    public void testIsAvailableLocale() {
        assert LocaleUtils.isAvailableLocale(Locale.ENGLISH);
        assert LocaleUtils.isAvailableLocale(Locale.FRENCH);
        assert !LocaleUtils.isAvailableLocale(new Locale("abc"));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("GB");
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH);
        
        assert languagesByCountry.equals(expectedList);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedList = Arrays.asList(new Locale("en", "GB"));
        
        assert countriesByLanguage.equals(expectedList);
    }
    
    @Test
    public void testEmptyLocale() {
        Locale emptyLocale = LocaleUtils.toLocale("");
        
        assert emptyLocale.equals(Locale.ROOT);
    }

}
