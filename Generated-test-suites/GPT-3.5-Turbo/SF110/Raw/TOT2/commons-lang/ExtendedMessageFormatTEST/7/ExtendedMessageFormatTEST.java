import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} pattern", Locale.US);
        assertNotNull(emf);
        assertEquals("Test {0} pattern", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} pattern");
        emf.applyPattern("New {0} pattern");
        assertEquals("New {0} pattern", emf.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} pattern");
        emf.applyPattern("Invalid {");
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0} pattern");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0} pattern");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0} pattern");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0} pattern");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }
}