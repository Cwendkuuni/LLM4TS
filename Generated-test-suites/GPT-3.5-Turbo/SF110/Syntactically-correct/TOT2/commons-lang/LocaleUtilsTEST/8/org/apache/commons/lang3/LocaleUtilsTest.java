package org.apache.commons.lang3;
import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleEmptyString() {
        Assert.assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid#format");
    }

    // Additional test cases for other scenarios in toLocale method can be added here

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Assert.assertEquals(LocaleUtils.localeLookupList(locale, locale).size(), 4);
    }

    // Additional test cases for other methods can be added here

}