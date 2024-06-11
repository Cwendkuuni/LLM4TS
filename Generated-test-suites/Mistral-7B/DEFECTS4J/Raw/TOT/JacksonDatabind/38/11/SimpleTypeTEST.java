import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringSimpleType.getBindings());
        assertNull(stringSimpleType.getSuperClass());
        assertNull(stringSimpleType.getSuperInterfaces());
        assertFalse(stringSimpleType.isContainerType());

        // Test with bindings
        TypeBindings bindings = TypeBindings.builder().bindValue("MyString", String.class).build();
        SimpleType myStringSimpleType = new SimpleType(String.class, bindings);
        assertEquals(String.class, myStringSimpleType.getRawClass());
        assertEquals(bindings, myStringSimpleType.getBindings());
        assertNull(myStringSimpleType.getSuperClass());
        assertNull(myStringSimpleType.getSuperInterfaces());
        assertFalse(myStringSimpleType.isContainerType());
        assertTrue(myStringSimpleType.getValueHandler() instanceof DefaultValueHandler);
        assertTrue(myStringSimpleType.getTypeHandler() instanceof DefaultTypeHandler);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType copyStringType = new SimpleType(originalStringType);
        assertEquals(String.class, copyStringType.getRawClass());
        assertEquals(originalStringType.getBindings(), copyStringType.getBindings());
        assertEquals(originalStringType.getSuperClass(), copyStringType.getSuperClass());
        assertArrayEquals(originalStringType.getSuperInterfaces(), copyStringType.getSuperInterfaces());
        assertFalse(copyStringType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object customValueHandler = new Object() {};
        SimpleType defaultStringType = new SimpleType(stringClass);
        SimpleType customStringType = new SimpleType(stringClass, null, null, null, null, customValueHandler, false);
        assertEquals(String.class, customStringType.getRawClass());
        assertNotSame(defaultStringType.getValueHandler(), customStringType.getValueHandler());
        assertEquals(customValueHandler, customStringType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidClass() {
        Class<?> invalidClass = new Object() {};
        new SimpleType(invalidClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForMapClass() {
        Class<Map<String, String>> mapClass = Map.class.getParameterizedType(String.class, String.class);
        new SimpleType((Class<?>) mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForCollectionClass() {
        Class<List<String>> collectionClass = List.class.getParameterizedType(String.class);
        new SimpleType((Class<?>) collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForArrayClass() {
        Class<String[]> arrayClass = String[].class;
        new SimpleType((Class<?>) arrayClass);
    }

    @Test
    public void testEqualsMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType1 = new SimpleType(stringClass);
        SimpleType stringSimpleType2 = new SimpleType(stringClass);
        SimpleType stringSimpleType3 = new SimpleType(Object.class);
        SimpleType stringSimpleType4 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);

        assertTrue(stringSimpleType1.equals(stringSimpleType1));
        assertTrue(stringSimpleType1.equals(stringSimpleType2));
        assertFalse(stringSimpleType1.equals(stringSimpleType3));
        assertFalse(stringSimpleType1.equals(stringSimpleType4));
    }
}