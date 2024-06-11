import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_ObjectNull() {
        try {
            HashCodeBuilder.reflectionHashCode(null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testReflectionHashCode_ObjectNotNull() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj, false);
        assertNotNull(hashCode);
    }

    @Test
    public void testAppend_booleanValue() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(builder.append(false).toHashCode(), hashCode * builder.iConstant + 1);
    }

    // More test cases for other methods can be added here

}