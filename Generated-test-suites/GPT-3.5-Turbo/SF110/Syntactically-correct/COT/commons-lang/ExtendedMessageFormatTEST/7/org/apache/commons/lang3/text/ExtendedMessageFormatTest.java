import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        // Test the constructor with pattern only
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
    }

    @Test
    public void testConstructor2() {
        // Test the constructor with pattern and locale
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
    }

    @Test
    public void testConstructor3() {
        // Test the constructor with pattern and registry
        // This test does not have a meaningful assertion since registry is not used in any other method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", null);
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
    }

    @Test
    public void testConstructor4() {
        // Test the constructor with pattern, locale, and registry
        // This test does not have a meaningful assertion since registry is not used in any other method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", Locale.US, null);
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
    }

    @Test
    public void testToPattern() {
        // Test the toPattern method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", emf.toPattern());
    }

    @Test
    public void testApplyPatternNoRegistry() {
        // Test the applyPattern method when registry is null
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.applyPattern("Hello, {0}, {1}!");
        assertEquals("Hello, World, 123!", emf.format(new Object[]{"World", 123}));
    }

    @Test
    public void testApplyPatternWithRegistry() {
        // Test the applyPattern method when registry is not null
        // This test does not have a meaningful assertion since registry is not used in any other method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", null);
        emf.applyPattern("Hello, {0}, {1}!");
        assertEquals("Hello, World, 123!", emf.format(new Object[]{"World", 123}));
    }

    @Test
    public void testSetFormat() {
        // Test the setFormat method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        try {
            emf.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        // Test the setFormatByArgumentIndex method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormats() {
        // Test the setFormats method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        try {
            emf.setFormats(new Format[]{null});
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        // Test the setFormatsByArgumentIndex method
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        try {
            emf.setFormatsByArgumentIndex(new Format[]{null});
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testEquals() {
        // Test the equals method
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Hello, {1}!");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Hello, {0}!", null);

        assertTrue(emf1.equals(emf1)); // Reflexive
        assertTrue(emf1.equals(emf2));
        assertTrue(emf2.equals(emf1));
        assertFalse(emf1.equals(emf3));
        assertFalse(emf3.equals(emf1));
        assertFalse(emf1.equals(emf4));
        assertFalse(emf4.equals(emf1));
        assertFalse(emf1.equals(null));
        assertFalse(emf1.equals("Hello, {0}!"));
    }

    @Test
    public void testHashCode() {
        // Test the hashCode method
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Hello, {1}!");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Hello, {0}!", null);

        assertEquals(emf1.hashCode(), emf2.hashCode());
        assertNotEquals(emf1.hashCode(), emf3.hashCode());
        assertNotEquals(emf1.hashCode(), emf4.hashCode());
    }
}