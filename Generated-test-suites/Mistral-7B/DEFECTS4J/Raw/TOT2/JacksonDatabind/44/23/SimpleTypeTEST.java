import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertEquals(null, stringType.getSuperClass());
        assertEquals(null, stringType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        // Assuming we have an instance of SimpleType named originalType
        SimpleType copyType = new SimpleType(originalType);
        assertEquals(originalType.getRawClass(), copyType.getRawClass());
        assertEquals(originalType.getBindings(), copyType.getBindings());
        assertEquals(originalType.getSuperClass(), copyType.getSuperClass());
        assertEquals(originalType.getSuperInterfaces(), copyType.getSuperInterfaces());
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertEquals(null, stringType.getSuperClass());
        assertEquals(null, stringType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        Class<?> invalidClass = new Class<?>(){};
        try {
            SimpleType type = SimpleType.construct(invalidClass);
        } catch (IllegalArgumentException e) {
            // expected exception
            assertEquals("Can not construct SimpleType for an array (class: " + invalidClass.getName() + ")", e.getMessage());
        }
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType integerType = new SimpleType(integerClass);
        SimpleType narrowedType = stringType._narrow(integerClass);
        assertEquals(integerClass, narrowedType.getRawClass());
        assertEquals(stringType.getBindings(), narrowedType.getBindings());
        assertEquals(stringType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(stringType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
    }
}