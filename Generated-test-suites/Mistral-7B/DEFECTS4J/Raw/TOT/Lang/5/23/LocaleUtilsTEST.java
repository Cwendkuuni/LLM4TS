package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.CompareToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private final String input;
    private final Locale expected;

    public LocaleUtilsTest(String input, Locale expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: toLocale({0}) = {1}")
    public static List<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"en", new Locale("en", "")},
                {"en_GB", new Locale("en", "GB")},
                {"en_GB_XXX", new Locale("en", "GB", "XXX")},
                {"fr_CA", new Locale("fr", "CA")},
                {"fr_CA_XXX", new Locale("fr", "CA", "XXX")},
                {"fr", new Locale("fr", "")},
                {"fr_", new Locale("fr", "")},
                {"fr_XXX", new Locale("fr", "")},
                {"fr_XXX_XXX", new Locale("fr", "")},
                {"", null},
                {"en_ZZ", null},
                {"en_ZZ_XXX", null},
                {"fr_ZZ", null},
                {"fr_ZZ_XXX", null},
                {"en_GB_en_US", null},
                {"en_US_en_GB", null},
                {"en_GB_fr_FR", null},
                {"fr_FR_en_GB", null},
                {"fr_FR_fr_FR", null},
                {"fr_FR_fr_FR_fr_FR", null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals(CompareToStringBuilder.compareToString(expected), actual.toString());
    }

    // Add test methods for other methods in the LocaleUtils class here

    // Test method for localeLookupList()
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);

        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(new Locale("fr", "CA")));
        assertTrue(lookupList.contains(new Locale("fr", "")));
        assertTrue(lookupList.contains(Locale.getDefault()));

        Locale defaultLocale = Locale.getDefault();
        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(5, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(new Locale("fr", "CA")));
        assertTrue(lookupList.contains(new Locale("fr", "")));
        assertTrue(lookupList.contains(defaultLocale));
        assertTrue(lookupList.contains(locale)); // Test for duplicate
    }

    // Test method for availableLocaleList() and availableLocaleSet()
    @Test
    public void testAvailableLocale() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();

        // Test size
        assertEquals(190, locales.size());
        assertEquals(190, localeSet.size());

        // Test contains
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(localeSet.contains(Locale.ENGLISH));

        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(localeSet.contains(Locale.FRENCH));

        // Test isAvailableLocale()
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_ZZ", "ZZ")));
    }
}