import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    
    private ExtendedMessageFormat extendedMessageFormat;
    
    @Before
    public void setup() {
        Map<String, FormatFactory> registry = new HashMap<>();
        // Add FormatFactory instances to the registry if needed for testing
        
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault(), registry);
    }
    
    @Test
    public void testToPattern() {
        assertEquals("Test {0}", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        extendedMessageFormat.applyPattern("New Test {0}");
        assertEquals("New Test {0}", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testGetFormat() {
        Format customFormat = extendedMessageFormat.getFormat("custom");
        assertNotNull(customFormat);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(new Format[] {});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[] {});
    }
}