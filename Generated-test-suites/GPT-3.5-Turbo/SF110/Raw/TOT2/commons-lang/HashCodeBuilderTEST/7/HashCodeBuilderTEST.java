import org.junit.*;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        int initial = 17;
        int multiplier = 37;
        Object object = new Object();
        int expected = object.hashCode();
        
        int result = HashCodeBuilder.reflectionHashCode(initial, multiplier, object);
        
        assertEquals(expected, result);
    }

    @Test
    public void testReflectionHashCodeWithNullObject() {
        int initial = 17;
        int multiplier = 37;
        Object object = null;
        
        assertThrows(IllegalArgumentException.class, () -> {
            HashCodeBuilder.reflectionHashCode(initial, multiplier, object);
        });
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;

        HashCodeBuilder result = builder.append(value);

        assertEquals(37, result.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};

        HashCodeBuilder result = builder.append(array);

        assertEquals(1440759713, result.toHashCode());
    }

    // Add more individual test cases as needed for other public methods

}