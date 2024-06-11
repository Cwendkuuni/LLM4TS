package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        try {
            LocaleUtils.toLocale("enGB");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: enGB", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("ENG");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: ENG", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("en_GBx");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GBx", e.getMessage());
        }
        
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(defaultLocale);

        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
        assertEquals(expectedList.subList(0, 2), LocaleUtils.localeLookupList(locale));

        assertEquals(Collections.singletonList(defaultLocale), LocaleUtils.localeLookupList(null, defaultLocale));
        assertEquals(Collections.singletonList(defaultLocale), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales()).stream()
                                    .filter(locale -> locale.getCountry().equals("US") && locale.getVariant().isEmpty())
                                    .collect(Collectors.toList());
        assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales()).stream()
                                    .filter(locale -> locale.getLanguage().equals("en") 
                                                      && !locale.getCountry().isEmpty()
                                                      && locale.getVariant().isEmpty())
                                    .collect(Collectors.toList());
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}