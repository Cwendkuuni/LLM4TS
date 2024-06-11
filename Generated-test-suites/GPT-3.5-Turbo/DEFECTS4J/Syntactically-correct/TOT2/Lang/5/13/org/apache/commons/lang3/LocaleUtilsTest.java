import org.junit.Test;
import org.junit.Assert;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Assert.assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        
        // Edge cases
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Error scenarios
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("EN");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        
        // Typical use case
        Assert.assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        // Edge cases
        Assert.assertEquals(Arrays.asList(locale, new Locale("en")), LocaleUtils.localeLookupList(locale, new Locale("en")));
        
        // Error scenarios
        try {
            LocaleUtils.localeLookupList(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // More test cases for other methods can be added here

}