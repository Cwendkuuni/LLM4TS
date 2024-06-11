import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testExtendedMessageFormat_constructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }
    
    @Test
    public void testExtendedMessageFormat_constructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", Locale.US);
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }
    
    @Test
    public void testExtendedMessageFormat_constructor3() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", null);
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }
    
    @Test
    public void testExtendedMessageFormat_constructor4() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", Locale.US, null);
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }
    
    @Test
    public void testExtendedMessageFormat_toPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        assertEquals("Test Pattern", emf.toPattern());
    }
    
    @Test
    public void testExtendedMessageFormat_applyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormat_setFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormat_setFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormat_setFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(new Format[] {null});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testExtendedMessageFormat_setFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(new Format[] {null});
    }
    
    @Test
    public void testExtendedMessageFormat_equals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0}");
        assertTrue(emf1.equals(emf1));
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testExtendedMessageFormat_hashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNotNull(emf.hashCode());
    }
}