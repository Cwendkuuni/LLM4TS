
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message", Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message", null);
        assertNotNull(format);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        assertEquals("Test {0} message", format.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        format.applyPattern("New {0} message");
        assertEquals("New {0} message", format.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        format.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        format.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        format.setFormats(new Format[] {null});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        format.setFormatsByArgumentIndex(new Format[] {null});
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Test {0} message");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Test {0} message");
        assertTrue(format1.equals(format2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0} message");
        assertNotNull(format.hashCode());
    }

}
