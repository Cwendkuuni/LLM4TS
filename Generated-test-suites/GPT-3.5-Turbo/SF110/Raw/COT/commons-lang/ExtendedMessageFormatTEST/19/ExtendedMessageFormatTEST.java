import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.Map;
import java.text.Format;
import java.text.FormatFactory;
import java.text.ParsePosition;
import java.util.ArrayList;

public class ExtendedMessageFormatTest {

    // Test applyPattern method with registry as null
    @Test
    public void testApplyPatternWithNullRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello, {0}");
        Assert.assertEquals("Hello, {0}", emf.toPattern());
    }

    // Test applyPattern method with registry as empty
    @Test
    public void testApplyPatternWithEmptyRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), Map.of());
        emf.applyPattern("Hello, {0}");
        Assert.assertEquals("Hello, {0}", emf.toPattern());
    }

    // Test applyPattern method with custom format
    @Test
    public void testApplyPatternWithCustomFormat() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return null;
            }
        };
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), Map.of("custom", factory));
        emf.applyPattern("Hello, {0,custom}");
        Assert.assertEquals("Hello, {0,custom}", emf.toPattern());
    }

    // Test setFormat method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormat(0, null);
    }

    // Test setFormatByArgumentIndex method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatByArgumentIndex(0, null);
    }

    // Test setFormats method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormats(null);
    }

    // Test setFormatsByArgumentIndex method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(null);
    }

    // Test equals method
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        Assert.assertTrue(emf1.equals(emf2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Assert.assertEquals(emf.hashCode(), emf.hashCode());
    }

    // Test parseFormatDescription method
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "Hello, {0,number,#.00}!";
        ParsePosition pos = new ParsePosition(9);
        String formatDescription = emf.parseFormatDescription(pattern, pos);
        Assert.assertEquals("number,#.00", formatDescription);
    }

    // Test insertFormats method
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "Hello, {0,number,#.00}!";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("custom");
        String newPattern = emf.insertFormats(pattern, customPatterns);
        Assert.assertEquals("Hello, {0,number,#.00,custom}!", newPattern);
    }

    // Test seekNonWs method
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "   Hello   ";
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs(pattern, pos);
        Assert.assertEquals(3, pos.getIndex());
    }

    // Test next method
    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        emf.next(pos);
        Assert.assertEquals(1, pos.getIndex());
    }

    // Test appendQuotedString method
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "Hello, 'World'";
        ParsePosition pos = new ParsePosition(9);
        StringBuilder result = emf.appendQuotedString(pattern, pos, new StringBuilder(), true);
        Assert.assertEquals("World", result.toString());
    }

    // Test getQuotedString method
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        String pattern = "Hello, 'World'";
        ParsePosition pos = new ParsePosition(9);
        emf.getQuotedString(pattern, pos, true);
        Assert.assertEquals(11, pos.getIndex());
    }

    // Test containsElements method with null collection
    @Test
    public void testContainsElementsWithNullCollection() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        boolean result = emf.containsElements(null);
        Assert.assertFalse(result);
    }

    // Test containsElements method with empty collection
    @Test
    public void testContainsElementsWithEmptyCollection() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        boolean result = emf.containsElements(new ArrayList<>());
        Assert.assertFalse(result);
    }

    // Test containsElements method with collection having null elements
    @Test
    public void testContainsElementsWithCollectionHavingNullElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> collection = new ArrayList<>();
        collection.add(null);
        collection.add("a");
        boolean result = emf.containsElements(collection);
        Assert.assertTrue(result);
    }
}