import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", Locale.ENGLISH);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern", registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        emf.applyPattern("Test Applied Pattern");
        String pattern = emf.toPattern();
        assertEquals("Test Applied Pattern", pattern);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        try {
            emf.setFormat(0, new DecimalFormat("#.00"));
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        try {
            emf.setFormats(new Format[]{new DecimalFormat("#.00")});
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        try {
            emf.setFormatByArgumentIndex(0, new DecimalFormat("#.00"));
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test Pattern");
        try {
            emf.setFormatsByArgumentIndex(new Format[]{new DecimalFormat("#.00")});
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}