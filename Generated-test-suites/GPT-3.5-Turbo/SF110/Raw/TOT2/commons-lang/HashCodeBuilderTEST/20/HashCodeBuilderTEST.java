import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCode() {
        Object obj1 = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj1);
        assertNotNull(hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The object to build a hash code for must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testAppendBooleanValue() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode > 0);
    }
    
    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode > 0);
    }
    
    @Test
    public void testAppendByteValue() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode > 0);
    }
    
    // Additional test cases for other methods can be added here
    
}