import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructor3() {
        HashMap<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructor4() {
        HashMap<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault(), registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("NewTest");
        assertEquals("NewTest", emf.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals(emf.hashCode(), emf.hashCode());

    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}