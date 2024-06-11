import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test for typical use case
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        assertNotNull(hashCode);
        
        // Test for edge case with null object
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        // Test for typical use case
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true, null, "id");
        assertNotNull(hashCode);
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        // Test for typical use case
        builder.append(true);
        assertEquals(37, builder.toHashCode());
        
        // Test for opposite value
        builder.append(false);
        assertNotEquals(37, builder.toHashCode());
    }

    // Test cases for other append methods can be added similarly

    @Test
    public void testHashCodeBuilderConstructor() {
        // Test for typical use case
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        assertNotNull(builder);
        
        // Test for edge case with even initial value
        try {
            HashCodeBuilder builder2 = new HashCodeBuilder(16, 37);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test for edge case with even multiplier value
        try {
            HashCodeBuilder builder3 = new HashCodeBuilder(17, 38);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test cases for other methods can be added similarly

}