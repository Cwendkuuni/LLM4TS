import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertTrue(stringSimpleType.isContainerType()); // Should be false
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithMap() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType mapSimpleType = SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithCollection() {
        Class<List<String>> listClass = List.class;
        SimpleType listSimpleType = SimpleType.construct(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithArray() {
        int[] intArray = new int[5];
        Class<int[]> intArrayClass = intArray.getClass();
        SimpleType intArraySimpleType = SimpleType.construct(intArrayClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringBuilderSimpleType = new SimpleType(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringSimpleType.narrow(stringBuilderClass);
        assertNull(narrowedType);
    }

    @Test
    public void testNarrowMethodWithSameClass() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringSimpleType.narrow(stringClass);
        assertEquals(stringSimpleType, narrowedType);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringBuilderSimpleType = new SimpleType(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringBuilderSimpleType.narrow(stringClass);
        assertEquals(stringSimpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithDifferentClass() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Class<Integer> integerClass = Integer.class;
        JavaType narrowedType = stringSimpleType.narrow(integerClass);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object stringHandler = new Object();
        SimpleType stringWithHandlerSimpleType = stringSimpleType.withTypeHandler(stringHandler);
        assertEquals(stringSimpleType.getClass(), stringWithHandlerSimpleType.getClass());
        assertNotEquals(stringSimpleType.getRawType(), stringWithHandlerSimpleType.getRawType());
        assertNotNull(stringWithHandlerSimpleType.getTypeHandler());
        assertEquals(stringHandler, stringWithHandlerSimpleType.getTypeHandler());
    }
}