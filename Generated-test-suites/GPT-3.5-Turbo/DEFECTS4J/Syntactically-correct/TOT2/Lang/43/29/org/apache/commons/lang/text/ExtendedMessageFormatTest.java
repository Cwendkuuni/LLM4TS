import org.junit.Test;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithDefaultLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test", registry);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("Test");
        assertEquals("Test", extendedMessageFormat.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.setFormats(new Format[]{});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[]{});
    }
}