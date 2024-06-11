import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US);
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", null);
        assertEquals("Test", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", null);
        emf.applyPattern("NewTest");
        assertEquals("NewTest", emf.toPattern());
    }

    // Add more test cases for other public methods

}