package org.apache.commons.lang3;
import org.junit.Test;
import org.junit.Assert;

import java.util.*;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_emptyString() {
        Locale locale = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    // Additional edge cases and exception handling scenarios can be added here

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("en", "GB"), defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertTrue(list.contains(new Locale("en", "GB")));
        Assert.assertTrue(list.contains(new Locale("en", "")));
        Assert.assertTrue(list.contains(defaultLocale));
    }

    // Additional test cases for the remaining methods in LocaleUtils can be added here

}