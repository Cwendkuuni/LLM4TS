import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.text.Format;
import java.util.Locale;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("{0}");
        assertEquals("{0}", emf.toPattern());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format format = emf.getFormat("number");
        assertNull(format);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertTrue(emf1.equals(emf2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals(emf.hashCode(), emf.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormats(new Format[]{});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(new Format[]{});
    }

    @Test
    public void testHashCodeNotNullRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new FormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", registry);
        assertEquals(emf.hashCode(), emf.hashCode());
    }
}

class FormatFactory implements org.apache.commons.lang3.text.FormatFactory {
    @Override
    public Format getFormat(String name, String args, Locale locale) {
        return null;
    }
}