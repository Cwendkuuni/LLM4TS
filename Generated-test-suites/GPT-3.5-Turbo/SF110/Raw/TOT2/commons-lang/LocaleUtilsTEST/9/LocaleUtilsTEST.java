import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());

        // Edge cases
        locale = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());

        // Error scenario
        try {
            LocaleUtils.toLocale("invalid_locale");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US);
        Assert.assertEquals(4, result.size());

        // Edge cases
        result = LocaleUtils.localeLookupList(Locale.US, Locale.UK);
        Assert.assertEquals(4, result.size());

        // Error scenario
        result = LocaleUtils.localeLookupList(null, Locale.UK);
        Assert.assertTrue(result.isEmpty());
    }

    // Repeat the above structure for the remaining public methods

}