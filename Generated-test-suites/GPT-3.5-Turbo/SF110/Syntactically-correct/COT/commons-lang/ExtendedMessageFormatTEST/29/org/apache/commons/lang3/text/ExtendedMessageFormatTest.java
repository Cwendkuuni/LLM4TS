
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {

        // Create an instance of ExtendedMessageFormat with a pattern and locale
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", Locale.US);

        // Perform assertions to check the functionality of the constructor
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
        assertEquals("Hello, Java!", emf.format(new Object[]{"Java"}));
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {

        // Create an instance of ExtendedMessageFormat with a pattern and registry
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!", 
                Collections.singletonMap("date", new SimpleDateFormatFormatFactory()));

        // Perform assertions to check the functionality of the constructor
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
        assertEquals("Hello, Java!", emf.format(new Object[]{"Java"}));
    }

    @Test
    public void testToPattern() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Perform assertions to check the functionality of the toPattern() method
        assertEquals("Hello, {0}!", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Apply a new pattern
        emf.applyPattern("Hi, {0}!");

        // Perform assertion to check the functionality of the applyPattern() method
        assertEquals("Hi, {0}!", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Try to set a format
        emf.setFormat(0, new DecimalFormat("#,##0.00"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Try to set a format by argument index
        emf.setFormatByArgumentIndex(0, new DecimalFormat("#,##0.00"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Try to set formats
        emf.setFormats(new Format[] {new DecimalFormat("#,##0.00")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {

        // Create an instance of ExtendedMessageFormat with a pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");

        // Try to set formats by argument index
        emf.setFormatsByArgumentIndex(new Format[] {new DecimalFormat("#,##0.00")});
    }

    @Test
    public void testEquals() {

        // Create two instances of ExtendedMessageFormat with the same pattern and locale
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!", Locale.US);

        // Perform assertions to check that the two instances are equal
        assertTrue(emf1.equals(emf2));
        assertTrue(emf2.equals(emf1));

        // Create two instances of ExtendedMessageFormat with different patterns
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("Hi, {0}!");

        // Perform assertions to check that the two instances are not equal
        assertFalse(emf1.equals(emf3));
        assertFalse(emf2.equals(emf4));
    }

    @Test
    public void testHashCode() {

        // Create two instances of ExtendedMessageFormat with the same pattern and locale
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!", Locale.US);

        // Perform assertion to check that the hash codes of the two instances are equal
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }
}

