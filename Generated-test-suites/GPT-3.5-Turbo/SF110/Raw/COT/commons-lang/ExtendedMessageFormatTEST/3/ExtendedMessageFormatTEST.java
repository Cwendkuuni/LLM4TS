import static org.junit.Assert.*;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private static ExtendedMessageFormat extendedMessageFormat;
    
    @BeforeClass
    public static void setUp() {
        extendedMessageFormat = createExtendedMessageFormat();
    }
    
    private static ExtendedMessageFormat createExtendedMessageFormat() {
        String pattern = "Today is {0,date,yyyy/MM/dd}";
        return new ExtendedMessageFormat(pattern);
    }
    
    @Test
    public void testToPattern() {
        assertEquals("Today is {0,date,yyyy/MM/dd}", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        String newPattern = "It is {0,time,HH:mm} now";
        extendedMessageFormat.applyPattern(newPattern);
        assertEquals(newPattern, extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat sameExtendedMessageFormat = createExtendedMessageFormat();
        assertTrue(extendedMessageFormat.equals(sameExtendedMessageFormat));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat sameExtendedMessageFormat = createExtendedMessageFormat();
        assertEquals(extendedMessageFormat.hashCode(), sameExtendedMessageFormat.hashCode());
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
        extendedMessageFormat.setFormats(new Format[0]);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
    }
    
    
    @Test
    public void testGetFormat() {
        Map<String, Format> formatRegistry = new HashMap<>();
        formatRegistry.put("date", new java.text.SimpleDateFormat("yyyy/MM/dd"));
        formatRegistry.put("time", new java.text.SimpleDateFormat("HH:mm"));
        ExtendedMessageFormat extendedMessageFormatWithRegistry =
                new ExtendedMessageFormat("Today is {0,date} and it is {1,time}", new Locale("en"), formatRegistry);
        
        assertEquals(formatRegistry.get("date"), extendedMessageFormatWithRegistry.getFormat("date"));
        assertEquals(formatRegistry.get("time"), extendedMessageFormatWithRegistry.getFormat("time"));
        assertNull(extendedMessageFormatWithRegistry.getFormat("invalidFormat"));
    }
    
}