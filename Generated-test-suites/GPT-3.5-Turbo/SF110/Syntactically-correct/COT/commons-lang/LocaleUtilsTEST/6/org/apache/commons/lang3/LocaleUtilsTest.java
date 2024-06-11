
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNullString() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocaleEmptyString() {
        Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("#en_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("en_USa");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat5() {
        LocaleUtils.toLocale("en");
    }
    
    @Test
    public void testToLocaleValidFormat1() {
        Locale expected = new Locale("en");
        assertEquals(expected, LocaleUtils.toLocale("en"));
    }
    
    @Test
    public void testToLocaleValidFormat2() {
        Locale expected = new Locale("en", "US");
        assertEquals(expected, LocaleUtils.toLocale("en_US"));
    }
    
    @Test
    public void testToLocaleValidFormat3() {
        Locale expected = new Locale("en", "", "US");
        assertEquals(expected, LocaleUtils.toLocale("en__US"));
    }
    
    @Test
    public void testToLocaleValidFormat4() {
        Locale expected = new Locale("", "US");
        assertEquals(expected, LocaleUtils.toLocale("_US"));
    }
    
    @Test
    public void testLocaleLookupListNullLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testLocaleLookupListNullDefaultLocale() {
        Locale locale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, null));
    }
    
    // Generate test cases for other methods...
    
}

