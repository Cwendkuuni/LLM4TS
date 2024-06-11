import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        assertNotNull(format);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", Locale.getDefault());
        assertNotNull(format);
    }

    @Test
    public void testConstructor3() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", null);
        assertNotNull(format);
    }

    @Test
    public void testConstructor4() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", Locale.getDefault(), null);
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        String pattern = format.toPattern();
        assertEquals("Test", pattern);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.applyPattern("New");
        String pattern = format.toPattern();
        assertEquals("New", pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Test");
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        int hashCode = format.hashCode();
        assertEquals(1056, hashCode);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testApplyPatternWithQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.applyPattern("'{0}', '{1}'");
        String pattern = format.toPattern();
        assertEquals("'Test', '{1}'", pattern);
    }

    @Test
    public void testApplyPatternWithUnsupportedFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.applyPattern("{0,unsupported}");
        String pattern = format.toPattern();
        assertEquals("{0,unsupported}", pattern);
    }

    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        format.applyPattern("");
        String pattern = format.toPattern();
        assertEquals("", pattern);
    }
}