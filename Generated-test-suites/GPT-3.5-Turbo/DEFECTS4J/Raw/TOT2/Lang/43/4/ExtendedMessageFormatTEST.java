import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    // Test for constructor with default locale
    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }

    // Test for constructor with specified locale
    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", Locale.US);
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }

    // Test for constructor with registry
    @Test
    public void testConstructorWithRegistry() {
        Map registry = new HashMap();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", registry);
        assertNotNull(emf);
        assertEquals("Test Pattern", emf.toPattern());
    }

    // Test for applying pattern
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("New Test Pattern");
        assertEquals("New Test Pattern", emf.toPattern());
    }

    // Test for setting custom format
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        emf.setFormat(1, new DecimalFormat("#.##"));
    }

    // Test for getting custom format based on description
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,customFormat}");
        Format format = emf.getFormat("number,customFormat");
        assertNotNull(format);
        assertTrue(format instanceof CustomFormat);
    }

}