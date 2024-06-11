import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    @Test
    public void testConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNotNull(emf);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New {0}");
        assertEquals("New {0}", emf.toPattern());
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0}");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals(31, emf.hashCode());
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNull(emf.getFormat("Unknown"));
    }
    
    // Test for edge cases and exceptions can be added here
    
}