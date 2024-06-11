import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCodeWithNullObject() {
        int hash = HashCodeBuilder.reflectionHashCode(17, 37, null);
        assertEquals(0, hash);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hash = builder.toHashCode();
        assertNotEquals(0, hash);
    }
    
    @Test
    public void testAppendIntArray() {
        int[] array = {1, 2, 3, 4, 5};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hash = builder.toHashCode();
        assertNotEquals(0, hash);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}