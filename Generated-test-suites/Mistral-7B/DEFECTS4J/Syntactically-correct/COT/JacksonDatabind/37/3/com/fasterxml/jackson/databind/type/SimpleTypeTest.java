import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls);
        SimpleType copied = new SimpleType(original);
        assertEquals(original, copied);
    }

    // Test for _narrow method

    @Test
    public void testNarrowMethod() {
        Class<?> stringClass = String.class;
        Class<?> stringSubclass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowedType = simpleType._narrow(stringSubclass);
        assertNotNull(narrowedType);
        assertNotEquals(simpleType, narrowedType);
        assertEquals(stringSubclass, narrowedType._class);
    }

    // Test for isContainerType method

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowWithInvalidSubclass() {
        Class<?> stringClass = String.class;
        Class<?> intClass = int.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowedType = simpleType._narrow(intClass);
        assertNull(narrowedType);
    }
}