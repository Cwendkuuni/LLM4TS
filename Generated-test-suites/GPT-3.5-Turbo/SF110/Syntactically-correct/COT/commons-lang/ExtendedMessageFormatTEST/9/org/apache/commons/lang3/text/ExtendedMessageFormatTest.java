
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorPatternLocaleRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern", Locale.US, null);
        assertNotNull(emf);
        assertEquals("This is a test pattern", emf.toPattern());
    }
  
    @Test
    public void testConstructorPatternLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern", Locale.US);
        assertNotNull(emf);
        assertEquals("This is a test pattern", emf.toPattern());
    }
  
    @Test
    public void testConstructorPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        assertNotNull(emf);
        assertEquals("This is a test pattern", emf.toPattern());
    }
  
    @Test
    public void testConstructorPatternLocaleRegistryNullPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat(null, Locale.US, null);
        assertNotNull(emf);
        assertNull(emf.toPattern());
    }
  
    @Test
    public void testConstructorPatternLocaleNullPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat(null, Locale.US);
        assertNotNull(emf);
        assertNull(emf.toPattern());
    }
  
    @Test
    public void testConstructorPatternNullPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat(null);
        assertNotNull(emf);
        assertNull(emf.toPattern());
    }
  
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        assertEquals("This is a test pattern", emf.toPattern());
    }
  
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        emf.applyPattern("New test pattern");
        assertEquals("New test pattern", emf.toPattern());
    }
  
    @Test
    public void testEqualsSameObject() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        assertEquals(emf, emf);
    }
  
    @Test
    public void testEqualsDifferentClass() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        assertNotEquals(emf, "This is a test pattern");
    }
  
    @Test
    public void testEqualsDifferentPattern() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("This is a test pattern");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("This is another test pattern");
        assertNotEquals(emf1, emf2);
    }
  
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        assertEquals(312293888, emf.hashCode());
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        emf.setFormat(1, new DecimalFormat());
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        emf.setFormatByArgumentIndex(1, new DecimalFormat());
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        emf.setFormats(new Format[] { new DecimalFormat() });
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("This is a test pattern");
        emf.setFormatsByArgumentIndex(new Format[] { new DecimalFormat() });
    }
  
}

