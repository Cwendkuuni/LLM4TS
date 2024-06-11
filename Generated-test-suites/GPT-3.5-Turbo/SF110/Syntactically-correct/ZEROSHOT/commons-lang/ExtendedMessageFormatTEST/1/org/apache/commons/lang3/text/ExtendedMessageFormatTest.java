import static org.junit.Assert.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;

    @Before
    public void setUp() {
        format = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.getDefault(), null);
    }

    @After
    public void tearDown() {
        format = null;
    }

    @Test
    public void testToPattern() {
        String pattern = format.toPattern();
        assertEquals("Hello, {0}, {1}", pattern);
    }

    @Test
    public void testApplyPattern() {
        format.applyPattern("Hi, {0}, {1}");
        String pattern = format.toPattern();
        assertEquals("Hi, {0}, {1}", pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        format.setFormat(0, new DecimalFormat("#,##0.00"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        format.setFormatByArgumentIndex(0, new DecimalFormat("#,##0.00"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        format.setFormats(new Format[] {new DecimalFormat("#,##0.00")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        format.setFormatsByArgumentIndex(new Format[] {new DecimalFormat("#,##0.00")});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.getDefault(), null);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.getDefault(), null);
        assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.getDefault(), null);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.getDefault(), null);
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testGetFormat() {
        Format format1 = format.getFormat("number");
        assertNull(format1);
    }

    @Test
    public void testReadArgumentIndex() {
        ParsePosition pos = new ParsePosition(0);
        int index = format.readArgumentIndex("Hello, {0}, {1}", pos);
        assertEquals(0, index);
    }

    @Test
    public void testParseFormatDescription() {
        ParsePosition pos = new ParsePosition(0);
        String description = format.parseFormatDescription("Hello, {0,number,#.00}, {1,date,yyyy-MM-dd}", pos);
        assertEquals("number,#.00", description);
    }

    @Test
    public void testInsertFormats() {
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add(null);
        String pattern = format.insertFormats("Hello, {0}, {1}", customPatterns);
        assertEquals("Hello, {0}, {1}", pattern);
    }

    @Test
    public void testSeekNonWs() {
        ParsePosition pos = new ParsePosition(0);
        format.seekNonWs("Hello, {0}, {1}", pos);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testNext() {
        ParsePosition pos = new ParsePosition(0);
        ParsePosition nextPos = format.next(pos);
        assertEquals(1, nextPos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        ParsePosition pos = new ParsePosition(0);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = format.appendQuotedString("Hello, '{0}, {1}'", pos, sb, true);
        assertEquals("Hello, {0}, {1}", result.toString());
    }

    @Test
    public void testGetQuotedString() {
        ParsePosition pos = new ParsePosition(0);
        format.getQuotedString("Hello, '{0}, {1}'", pos, true);
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testContainsElements() {
        List<String> list = new ArrayList<>();
        list.add("element");
        boolean contains = format.containsElements(list);
        assertTrue(contains);
    }
}