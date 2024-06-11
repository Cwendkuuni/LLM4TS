import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("Test {0,number}");
        assertEquals("Test {0,number}", emf.toPattern());
    }

    @Test
    public void testSetFormatThrowsException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormat(0, null);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndexThrowsException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsThrowsException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormats(new Format[1]);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndexThrowsException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormatsByArgumentIndex(new Format[1]);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}