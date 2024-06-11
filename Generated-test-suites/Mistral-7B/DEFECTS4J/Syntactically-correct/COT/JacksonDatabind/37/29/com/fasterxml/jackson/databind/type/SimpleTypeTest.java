import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals("Type should be instantiated with the given class", cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should not be able to construct SimpleType with array class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should not be able to construct SimpleType with Map class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should not be able to construct SimpleType with Collection class");
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals("Type should be instantiated with the given class", cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not be able to construct SimpleType with array class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not be able to construct SimpleType with Map class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not be able to construct SimpleType with Collection class");
    }

    @Test
    public void testNarrow() {
        Class<?> parentClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType parentSimpleType = new SimpleType(parentClass);
        SimpleType subSimpleType = new SimpleType(subClass);

        SimpleType narrowedType = parentSimpleType._narrow(subClass);
        assertEquals("Narrowed type should be the same as the sub-type instance", subSimpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> parentClass = Object.class;
        Class<?> subClass = int[].class;
        SimpleType parentSimpleType = new SimpleType(parentClass);
        SimpleType subSimpleType = new SimpleType(subClass);

        parentSimpleType._narrow(subClass);
        fail("Should throw an IllegalArgumentException when trying to narrow a parent type to an invalid subclass");
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String toStringResult = simpleType.toString();
        String expectedResult = "[simple type, class " + cls.getName() + "]";
        assertEquals("toString method should return the expected result", expectedResult, toStringResult);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);

        assertFalse("Two different SimpleTypes should not be equal", simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue("Two equal SimpleTypes should be equal", simpleType1.equals(simpleType3));
    }

}