import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
    // Test case for the constructor that takes a Class argument
    @Test
    public void testSimpleTypeConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("java.lang.Integer", intType.getClassName());
        assertFalse(intType.isContainerType());
    }

    // Test case for the method _narrow() that throws an exception when the subclass is not a subtype
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType stringType = intType._narrow(String.class);
    }

    // Test case for the method _narrow() that returns the same instance when the subclass is the same
    @Test
    public void testNarrowMethodWithSameSubclass() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        SimpleType intType3 = intType._narrow(intClass);
        assertEquals(intType, intType3);
    }
}