import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithStringLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        assertEquals("Hello {0}, welcome to {1}", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithStringLocaleRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}", Locale.US, null);
        assertEquals("Hello {0}, welcome to {1}", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithStringRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}", null);
        assertEquals("Hello {0}, welcome to {1}", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithStringLocaleRegistryRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}", Locale.US, null);
        assertEquals("Hello {0}, welcome to {1}", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        assertEquals("Hello {0}, welcome to {1}", format.toPattern());
    }

    @Test
    public void testApplyPatternNoRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        format.applyPattern("Goodbye {0}, see you at {1}");
        assertEquals("Goodbye {0}, see you at {1}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}", null);
        format.applyPattern("Goodbye {0}, see you at {1}");
        assertEquals("Goodbye {0}, see you at {1}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        format.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        format.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}");
        assertEquals(-346729103, format.hashCode());
    }

}