import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithDefaultLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message");
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message", Locale.getDefault());
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message", registry);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message", Locale.getDefault(), registry);
        assertNotNull(extendedMessageFormat);
    }

    // Edge case: Empty pattern
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
    }

    // Edge case: Invalid format argument index
    @Test(expected = IllegalArgumentException.class)
    public void testReadInvalidArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{0");
    }

    // Edge case: Unterminated format element
    @Test(expected = IllegalArgumentException.class)
    public void testReadUnterminatedFormatElement() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{0, number");
    }

    // Error scenario: Unsupported operation - setFormats
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message");
        extendedMessageFormat.setFormats(new Format[]{});
    }

    // Error scenario: Unsupported operation - setFormatByArgumentIndex
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test Message");
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }

}