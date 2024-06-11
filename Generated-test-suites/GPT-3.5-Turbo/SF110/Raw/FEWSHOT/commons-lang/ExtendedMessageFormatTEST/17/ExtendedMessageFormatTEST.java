import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.text.Format;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault());
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", registry);
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format format = null;
        try {
            emf.setFormat(0, format);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format format = null;
        try {
            emf.setFormatByArgumentIndex(0, format);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format[] formats = {};
        try {
            emf.setFormats(formats);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format[] formats = {};
        try {
            emf.setFormatsByArgumentIndex(formats);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertEquals(true, emf1.equals(emf2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals(31, emf.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format format = emf.getFormat("");
        assertNull(format);
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ParsePosition pos = new ParsePosition(0);
        assertEquals(0, emf.readArgumentIndex(pattern, pos));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ParsePosition pos = new ParsePosition(0);
        emf.parseFormatDescription(pattern, pos);
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ArrayList<String> customPatterns = new ArrayList<>();
        assertEquals("", emf.insertFormats(pattern, customPatterns));
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs(pattern, pos);
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        emf.next(pos);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        boolean escapingOn = true;
        emf.appendQuotedString(pattern, pos, appendTo, escapingOn);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "";
        ParsePosition pos = new ParsePosition(0);
        boolean escapingOn = true;
        emf.getQuotedString(pattern, pos, escapingOn);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> coll1 = new ArrayList<>();
        ArrayList<String> coll2 = new ArrayList<>();
        coll2.add(null);
        assertEquals(false, emf.containsElements(coll1));
        assertEquals(false, emf.containsElements(coll2));
    }

}