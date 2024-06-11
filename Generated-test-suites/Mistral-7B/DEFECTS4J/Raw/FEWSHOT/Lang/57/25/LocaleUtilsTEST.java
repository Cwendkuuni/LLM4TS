package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("de", "DE"), LocaleUtils.toLocale("de_DE"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));

        // Test empty input
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input
        assertFalse(LocaleUtils.toLocale("en_US_v1.0").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_US_v1.0").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en US").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_US ").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_ GB_v1.0").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_ GB_v1.0").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en GB").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_ GB ").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_ GB_v1.0 xxx").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en_ GB_v1.0 xxx").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en GB xxx").getClass().isAssignableFrom(Locale.class));
        assertFalse(LocaleUtils.toLocale("en GB xxx").getClass().isAssignableFrom(Locale.class));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List emptyList = LocaleUtils.localeLookupList(null);
        assertEquals(0, emptyList.size());

        // Test single element list
        List singleElementList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(1, singleElementList.size());
        assertSame(singleElementList.get(0), new Locale("en", "US"));

        // Test multiple element list
        List multipleElementList = LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale("fr", "FR"));
        assertEquals(3, multipleElementList.size());
        assertSame(multipleElementList.get(0), new Locale("en", "US"));
        assertSame(multipleElementList.get(1), new Locale("en", ""));
        assertSame(multipleElementList.get(2), new Locale("fr", "FR"));

        // Test default locale
        List defaultLocaleList = LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale(""));
        assertEquals(4, defaultLocaleList.size());
        assertSame(defaultLocaleList.get(0), new Locale("en", "US"));
        assertSame(defaultLocaleList.get(1), new Locale("en", ""));
        assertSame(defaultLocaleList.get(2), new Locale("", ""));
        assertSame(defaultLocaleList.get(3), new Locale(""));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(154, availableLocaleList.size());

        // Check first few elements
        assertEquals(new Locale("ar", "AE"), availableLocaleList.get(0));
        assertEquals(new Locale("ar", "BH"), availableLocaleList.get(1));
        assertEquals(new Locale("ar", "DZ"), availableLocaleList.get(2));
        assertEquals(new Locale("ar", "EG"), availableLocaleList.get(3));
        assertEquals(new Locale("ar", "IQ"), availableLocaleList.get(4));
        assertEquals(new Locale("ar", "JO"), availableLocaleList.get(5));
        assertEquals(new Locale("ar", "KW"), availableLocaleList.get(6));
        assertEquals(new Locale("ar", "LB"), availableLocaleList.get(7));
        assertEquals(new Locale("ar", "LY"), availableLocaleList.get(8));
        assertEquals(new Locale("ar", "OM"), availableLocaleList.get(9));
        assertEquals(new Locale("ar", "QA"), availableLocaleList.get(10));
        assertEquals(new Locale("ar", "SA"), availableLocaleList.get(11));
        assertEquals(new Locale("ar", "SD"), availableLocaleList.get(12));
        assertEquals(new Locale("ar", "SY"), availableLocaleList.get(13));
        assertEquals(new Locale("ar", "TN"), availableLocaleList.get(14));
        assertEquals(new Locale("ar", "YE"), availableLocaleList.get(15));

        // Check last few elements
        assertEquals(new Locale("uz", "UZ"), availableLocaleList.get(151));
        assertEquals(new Locale("uz", "UZ"), availableLocaleList.get(152));
        assertEquals(new Locale("vi", "VN"), availableLocaleList.get(153));
        assertEquals(new Locale("vi", "VN"), availableLocaleList.get(154));
    }
}