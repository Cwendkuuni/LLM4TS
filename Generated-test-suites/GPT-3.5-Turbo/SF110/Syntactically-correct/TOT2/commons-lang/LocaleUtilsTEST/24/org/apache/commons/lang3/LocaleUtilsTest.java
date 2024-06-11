
package org.apache.commons.lang3;

import org.junit.*;
import java.util.*;

public class LocaleUtilsTest {
    
    // Test toLocale method with typical input
    @Test
    public void testToLocaleTypicalInput() {
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
    }
    
    // Test toLocale method with empty string
    @Test
    public void testToLocaleEmptyString() {
        Locale locale = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }
    
    // Test toLocale method with invalid locale format
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid_format");
    }
}
