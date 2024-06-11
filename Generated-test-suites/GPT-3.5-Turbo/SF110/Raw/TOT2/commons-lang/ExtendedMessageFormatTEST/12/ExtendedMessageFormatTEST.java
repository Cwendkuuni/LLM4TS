import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault());
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello {0}");
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> emf.setFormat(0, null));
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

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.getFormat("Test"));
    }

}