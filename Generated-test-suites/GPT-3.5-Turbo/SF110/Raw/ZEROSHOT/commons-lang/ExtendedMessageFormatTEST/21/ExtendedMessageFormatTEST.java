import org.junit.Test;
import org.junit.Assert;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        String result = emf.toPattern();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault());
        String result = emf.toPattern();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testConstructor3() {
        Map<String, ? extends FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        String result = emf.toPattern();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testConstructor4() {
        Map<String, ? extends FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault(), registry);
        String result = emf.toPattern();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Test");
        String result = emf.toPattern();
        Assert.assertEquals("Test", result);
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
        emf.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        boolean result = emf1.equals(emf2);
        Assert.assertTrue(result);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        int result = emf.hashCode();
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Format result = emf.getFormat("");
        Assert.assertNull(result);
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        int result = emf.readArgumentIndex("", pos);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String result = emf.parseFormatDescription("", pos);
        Assert.assertEquals("", result);
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("Test");
        String result = emf.insertFormats("", customPatterns);
        Assert.assertEquals("", result);
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs("", pos);
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        ParsePosition result = emf.next(pos);
        Assert.assertNotNull(result);
    }

    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        StringBuilder result = emf.appendQuotedString("", pos, appendTo, true);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        emf.getQuotedString("", pos, true);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Collection<?> coll = new ArrayList<>();
        boolean result = emf.containsElements(coll);
        Assert.assertFalse(result);
    }
}