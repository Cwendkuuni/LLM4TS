import org.junit.*;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorOneArg() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
        assertNotNull(format);
    }

    @Test
    public void testConstructorTwoArgs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault());
        assertNotNull(format);
    }

    @Test
    public void testConstructorThreeArgs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        assertNotNull(format);
    }

    @Test
    public void testConstructorAllArgs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
        String pattern = format.toPattern();
        assertEquals("", pattern);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        format.applyPattern("NewPattern");
        String pattern = format.toPattern();
        assertEquals("NewPattern", pattern);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        try {
            format.setFormat(0, null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        try {
            format.setFormatByArgumentIndex(0, null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        try {
            format.setFormats(null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        try {
            format.setFormatsByArgumentIndex(null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        int hashCode = format.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        Format result = format.getFormat("");
        assertNull(result);
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        int index = format.readArgumentIndex("Pattern", pos);
        assertEquals(0, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexInvalidIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        format.readArgumentIndex("Pattern", pos);
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        String description = format.parseFormatDescription("Pattern", pos);
        assertEquals("", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionUnterminatedElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        format.parseFormatDescription("Pattern", pos);
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ArrayList<String> customPatterns = new ArrayList<String>();
        customPatterns.add(null);
        String result = format.insertFormats("Pattern", customPatterns);
        assertEquals("Pattern", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertFormatsInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ArrayList<String> customPatterns = new ArrayList<String>();
        format.insertFormats("Pattern", customPatterns);
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        format.seekNonWs("Pattern", pos);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        ParsePosition result = format.next(pos);
        assertNotNull(result);
    }

    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        StringBuilder result = format.appendQuotedString("Pattern", pos, appendTo, true);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendQuotedStringUnterminatedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        format.appendQuotedString("Pattern", pos, null, true);
    }

    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ParsePosition pos = new ParsePosition(0);
        format.getQuotedString("Pattern", pos, true);
    }

    @Test
    public void testContainsElementsEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ArrayList<Object> coll = new ArrayList<Object>();
        boolean containsElements = format.containsElements(coll);
        assertFalse(containsElements);
    }

    @Test
    public void testContainsElementsNotEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.getDefault(), null);
        ArrayList<Object> coll = new ArrayList<Object>();
        coll.add(null);
        boolean containsElements = format.containsElements(coll);
        assertTrue(containsElements);
    }
}