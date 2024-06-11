import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null, false, null, new String[]{"test"});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReflectionHashCodeWithInvalidMultiplier() {
        try {
            HashCodeBuilder builder = new HashCodeBuilder(10, 20);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAppendBooleanArrayWithNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((boolean[]) null);
        assertEquals(0, builder.toHashCode());
    }

    // Add more test cases for other methods

}