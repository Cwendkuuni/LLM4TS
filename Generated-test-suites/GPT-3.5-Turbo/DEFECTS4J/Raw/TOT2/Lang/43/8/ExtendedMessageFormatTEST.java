import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
    }

    // Test constructor with locale
    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US);
        assertNotNull(emf);
    }

    // Test constructor with registry
    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
    }

    // Test constructor with locale and registry
    @Test
    public void testConstructorWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US, registry);
        assertNotNull(emf);
    }

    // Test toPattern method
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    // Test applyPattern method
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("NewTest");
        assertEquals("NewTest", emf.toPattern());
    }

    // Test invalid argument index
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.format(new Object());
    }

    // Test unfinished format element
    @Test(expected = IllegalArgumentException.class)
    public void testUnfinishedFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer");
        emf.format(new Object());
    }

    // Test unsupported setFormat method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormat(0, new DecimalFormat());
    }

    // Test unsupported setFormatByArgumentIndex method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    // Test unsupported setFormats method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormats(new Format[]{new DecimalFormat()});
    }

    // Test unsupported setFormatsByArgumentIndex method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
    }

    // Test getFormat method
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,integer}", Locale.US);
        Format format = emf.getFormat("number,integer");
        assertTrue(format instanceof NumberFormat);
    }

}