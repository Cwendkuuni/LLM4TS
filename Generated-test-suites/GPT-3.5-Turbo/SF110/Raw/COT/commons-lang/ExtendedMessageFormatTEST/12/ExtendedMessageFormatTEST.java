
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithStringLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault());
        assertNotNull(format);
    }
    
    @Test
    public void testConstructorWithStringLocaleRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault(), null);
        assertNotNull(format);
    }
    
    @Test
    public void testConstructorWithStringAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", null);
        assertNotNull(format);
    }
    
    @Test
    public void testConstructorWithStringLocaleRegistryAndPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.getDefault(), null);
        assertNotNull(format);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        String pattern = format.toPattern();
        assertEquals("Hello, {0}!", pattern);
    }
    
    @Test
    public void testToString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        String string = format.toString();
        assertTrue(string.contains("Hello, {0}!"));
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        assertTrue(format1.equals(format2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        int hashCode = format.hashCode();
        assertTrue(hashCode != 0);
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        Format formatObj = format.getFormat("0");
        assertNull(formatObj);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0!}");
        format.applyPattern("Hello, {0!}");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0");
        format.applyPattern("Hello, {0");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormats(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatsByArgumentIndex(null);
    }
}
