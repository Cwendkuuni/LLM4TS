import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] array = {true, false, true};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(121, builder.toHashCode());
    }

    // Additional test cases for other append methods can be added here

    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Additional test cases for reflection methods and other scenarios can be added here

}