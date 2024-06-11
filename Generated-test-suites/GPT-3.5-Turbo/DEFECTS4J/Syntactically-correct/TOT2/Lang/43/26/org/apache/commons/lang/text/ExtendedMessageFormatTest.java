import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hi {0}");
        assertEquals("Hi {0}", emf.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        String pattern = emf.toPattern();
        assertEquals("Hello {0}", pattern);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormat(0, null);
        });
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormatByArgumentIndex(0, null);
        });
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormats(new Format[] {null});
        });
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormatsByArgumentIndex(new Format[] {null});
        });
    }

}