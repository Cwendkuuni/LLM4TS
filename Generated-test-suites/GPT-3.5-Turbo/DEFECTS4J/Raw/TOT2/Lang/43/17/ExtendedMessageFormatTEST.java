import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setup() {
        extendedMessageFormat = new ExtendedMessageFormat("{0,number,#.##}", Locale.US);
    }

    @Test
    public void testApplyPattern() {
        assertNotNull(extendedMessageFormat);
        assertEquals("{0,number,#.##}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyInvalidPattern() {
        try {
            extendedMessageFormat.applyPattern("{0,number,##.#}");
            fail("Should throw IllegalArgumentException for invalid pattern");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormat() {
        try {
            extendedMessageFormat.setFormat(0, null);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormats() {
        try {
            extendedMessageFormat.setFormats(new Format[] {null});
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(new Format[] {null});
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetFormat() {
        assertNull(extendedMessageFormat.getFormat("unknown"));
    }

    @Test
    public void testGetFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat customFormat = new ExtendedMessageFormat("{0,number,#.##}", Locale.US, registry);
        assertNotNull(customFormat.getFormat("number"));
    }
}

class NumberFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String args, Locale locale) {
        return NumberFormat.getNumberInstance(locale);
    }
}