import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    @Test
    public void testReflectionHashCode() {
        // Test reflectionHashCode method with different parameters
        int hashCode1 = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        int hashCode2 = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true);
        
        // Assert that the two hash codes are not equal
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testReflectionHashCodeWithTransients() {
        // Test reflectionHashCode method with transients
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true);

        // Assert that the hash code is not zero
        assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        // Test reflectionHashCode method with excluded fields
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, "field1", "field2");

        // Assert that the hash code is not zero
        assertNotEquals(0, hashCode);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        // Test reflectionHashCode method with null object (should throw IllegalArgumentException)
        HashCodeBuilder.reflectionHashCode(17, 37, null);
    }
    
    @Test
    public void testReflectionHashCodeWithEmptyExcludeFields() {
        // Test reflectionHashCode method with empty exclude fields array
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null);

        // Assert that the hash code is not zero
        assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testAppendBoolean() {
        // Test append method with boolean value
        int hashCode = new HashCodeBuilder().append(true).toHashCode();

        // Assert that the hash code is not zero
        assertNotEquals(0, hashCode);
    }
    
    // Repeat the above test cases for other append methods
    
    
}