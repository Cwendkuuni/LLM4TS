import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_DefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
    }

    @Test
    public void testConstructor_WithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructor_WithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("NewTest");
        assertEquals(emf.toPattern(), "NewTest");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("{invalid-pattern}");
    }

    @Test
    public void testSetFormatMethods() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        try {
            emf.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // as expected
        }

        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // as expected
        }

        try {
            emf.setFormats(new Format[1]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // as expected
        }

        try {
            emf.setFormatsByArgumentIndex(new Format[1]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // as expected
        }
    }

}