import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_withPatternAndLocale_ShouldSetPatternAndLocale() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
    }
    
    @Test
    public void testConstructor_withPatternLocaleAndRegistry_ShouldSetPatternLocaleAndRegistry() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }
    
    @Test
    public void testApplyPattern_withValidPattern_ShouldSetPattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        
        format.applyPattern(pattern);
        
        assertEquals(pattern, format.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_withInvalidPattern_ShouldThrowIllegalArgumentException() {
        String pattern = "Hello, {0";
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        
        format.applyPattern(pattern);
    }
    
    @Test
    public void testEquals_withEqualObjects_ShouldReturnTrue() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern, locale);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern, locale);
        
        boolean result = format1.equals(format2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_withDifferentObjects_ShouldReturnFalse() {
        String pattern1 = "Hello, {0}!";
        String pattern2 = "Hello, {1}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, locale);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, locale);
        
        boolean result = format1.equals(format2);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_withEqualObjects_ShouldReturnSameHashCode() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern, locale);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern, locale);
        
        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_withDifferentObjects_ShouldReturnDifferentHashCodes() {
        String pattern1 = "Hello, {0}!";
        String pattern2 = "Hello, {1}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, locale);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern2, locale);
        
        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
}