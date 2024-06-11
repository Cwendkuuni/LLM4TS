import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }
    
    @Test
    public void testConstructorWithSpecifiedLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US);
        assertEquals("Test", emf.toPattern());
    }
    
    @Test
    public void testConstructorWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Collections.singletonMap("test", null));
        assertEquals("Test", emf.toPattern());
    }
    
    // Add more test cases for other public methods
    
    // Edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.applyPattern("Invalid {999}");
    }
    
    // Merge all test cases into the complete test file

}