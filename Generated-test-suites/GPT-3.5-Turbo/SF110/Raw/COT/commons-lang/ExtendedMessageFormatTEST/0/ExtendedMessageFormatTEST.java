import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("");
    }

    @Test
    public void testToPattern() {
        assertEquals("", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0}";

        extendedMessageFormat.applyPattern(pattern);

        assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("");

        assertTrue(extendedMessageFormat.equals(extendedMessageFormat));
        assertFalse(extendedMessageFormat.equals(null));
        assertFalse(extendedMessageFormat.equals(new String("test")));
        assertTrue(extendedMessageFormat.equals(extendedMessageFormat1));
    }

    @Test
    public void testHashCode() {
        int hashCode = extendedMessageFormat.hashCode();
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("");

        assertEquals(hashCode, extendedMessageFormat1.hashCode());
    }

}