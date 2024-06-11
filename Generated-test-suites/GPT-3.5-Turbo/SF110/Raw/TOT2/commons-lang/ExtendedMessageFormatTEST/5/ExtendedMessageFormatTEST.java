import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello {0}", Locale.ENGLISH);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello {0}", null);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello {0}");
        assertEquals("Hello {0}", extendedMessageFormat.toPattern());
    }

    // Add more test cases for other public methods

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello {0}");
        extendedMessageFormat.setFormat(0, null);
    }

    // Add more test cases for exception handling scenarios

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("Hello {0}");
        assertEquals(extendedMessageFormat1, extendedMessageFormat2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(extendedMessageFormat.hashCode());
    }

    // Add more test cases for other methods

}