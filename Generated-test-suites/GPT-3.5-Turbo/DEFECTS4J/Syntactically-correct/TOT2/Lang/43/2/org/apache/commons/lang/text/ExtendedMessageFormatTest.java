import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        assertNotNull(emf);
    }

    @Test
    public void testConstructorLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorLocaleRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message", Locale.US, registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        String newPattern = "New Test Message";
        emf.applyPattern(newPattern);
        assertEquals(newPattern, emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        emf.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        emf.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        emf.setFormats(new Format[]{new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        emf.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Message");
        assertEquals("Test Message", emf.toPattern());
    }
}