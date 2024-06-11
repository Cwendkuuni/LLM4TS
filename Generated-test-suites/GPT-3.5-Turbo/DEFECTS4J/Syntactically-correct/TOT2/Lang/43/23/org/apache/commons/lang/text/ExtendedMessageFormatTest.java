import org.junit.Test;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructorCustomLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", new Locale("en"));
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault(), registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New Pattern {0}");
        assertEquals("New Pattern {0}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(new Format[]{null});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(new Format[]{null});
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    // Add more test cases here for comprehensive testing

}