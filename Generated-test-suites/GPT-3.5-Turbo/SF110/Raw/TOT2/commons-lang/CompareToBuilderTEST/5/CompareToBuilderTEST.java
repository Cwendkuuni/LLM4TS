import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Test case for reflectionCompare method
        // Typical use case where two objects are compared
        assertEquals(0, CompareToBuilder.reflectionCompare("hello", "hello"));
        
        // Edge case where two different objects are compared
        assertNotEquals(0, CompareToBuilder.reflectionCompare("hello", 123));
        
        // Error scenario where one of the objects is null
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, "hello"));
    }

    // Add test cases for other methods in CompareToBuilder class...

}