package org.apache.commons.lang3;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LocaleUtilsTest {
  
    @Test
    public void testToLocale() {
        assertEquals(LocaleUtils.toLocale("en_US"), new Locale("en", "US"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithEmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("en#US");
    }

    // Add test cases for other methods and scenarios
}
