import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
        Assert.assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_countryVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
        Assert.assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("12");
    }

    @Test
    public void testLocaleLookupList_specificLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(defaultLocale, list.get(3));
    }

    // Additional test cases for other methods can be added here

}