import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Typical use case
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        assertEquals(990, hashCode);
        
        // Edge case - null object
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testReflectionHashCode2() {
        // Typical use case
        int hashCode = HashCodeBuilder.reflectionHashCode(new Object(), false);
        assertEquals(990, hashCode);
        
        // Edge case - empty exclude fields
        int[] excludeFields = {};
        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, excludeFields);
        assertEquals(990, hashCode);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(54, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int hashCode = builder.toHashCode();
        assertEquals(196, hashCode);
    }

    // Continue with test cases for other append methods

}