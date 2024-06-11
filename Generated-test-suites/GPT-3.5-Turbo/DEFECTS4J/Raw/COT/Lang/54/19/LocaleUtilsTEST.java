import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        // Valid locale strings
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("IllegalArgumentException should be thrown for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("");
            Assert.fail("IllegalArgumentException should be thrown for empty input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("IllegalArgumentException should be thrown for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_");
            Assert.fail("IllegalArgumentException should be thrown for incomplete input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("IllegalArgumentException should be thrown for incomplete input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        // Test with locale and defaultLocale
        Assert.assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale), LocaleUtils.localeLookupList(locale, defaultLocale));
        // Test with null locale
        Assert.assertEquals(Arrays.asList(defaultLocale), LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        Assert.assertNotNull(LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Assert.assertNotNull(LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Valid country code
        Assert.assertEquals(Arrays.asList(new Locale("fr", "CA")), LocaleUtils.languagesByCountry("CA"));
        // Invalid country code
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("invalid"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Valid language code
        Assert.assertEquals(Arrays.asList(new Locale("en", "GB")), LocaleUtils.countriesByLanguage("en"));
        // Invalid language code
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("invalid"));
    }
    
}