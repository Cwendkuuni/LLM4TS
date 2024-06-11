
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}");
        assertEquals("Hello, {0}", emf.toPattern());
    }
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", Locale.US);
        assertEquals("Hello, {0}", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", null);
        assertEquals("Hello, {0}", emf.toPattern());
    }
    
    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}", Locale.US, null);
        assertEquals("Hello, {0}", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
    }
    
    @Test
    public void testApplyPatternNoRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello, {0}");
        assertEquals("Hello, {0}", emf.toPattern());
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", null);
        emf.applyPattern("Hello, {0}");
        assertEquals("Hello, {0}", emf.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormats(new Format[0]);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(new Format[0]);
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}");
        assertEquals(emf.hashCode(), emf.hashCode());
    }
}
