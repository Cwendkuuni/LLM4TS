
import static org.junit.Assert.*;

import org.junit.Test;

public class ValidateTest {
    
    @Test
    public void testIsTrueWithDouble() {
        try {
            Validate.isTrue(true, "Value should be false", 5.0);
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testNotEmptyArray() {
        String[] array = new String[] {"one", "two"};
        String[] result = Validate.notEmpty(array);
        assertNotNull(result);
    }
    
    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        try {
            Validate.notEmpty(list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    // More test cases for other methods can be added here
    
}
