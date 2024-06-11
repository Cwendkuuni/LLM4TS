import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault(), registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormats(new Format[] {new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatsByArgumentIndex(new Format[] {new DecimalFormat()});
    }

    // Add more test cases for edge cases and exception handling scenarios

}