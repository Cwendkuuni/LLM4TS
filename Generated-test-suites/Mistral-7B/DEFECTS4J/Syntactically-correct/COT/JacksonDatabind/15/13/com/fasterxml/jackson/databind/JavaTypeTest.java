import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> rawType = Integer.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        assertEquals(rawType, javaType.getRawClass());
        assertEquals(0, javaType.hashCode());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isConcrete());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsingInvalidSubclass() {
        Class<Integer> rawType = Integer.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.narrowBy(String.class);
    }

    @Test
    public void testNarrowBy() {
        Class<Integer> rawType = Integer.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType narrowType = javaType.narrowBy(int[].class);
        assertTrue(int[].class.isAssignableFrom(javaType.getRawClass()));
        assertTrue(int[].class.isAssignableFrom(narrowType.getRawClass()));
        assertEquals(javaType.hashCode(), narrowType.hashCode());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isConcrete());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertTrue(javaType.isContainerType());
        assertTrue(javaType.isArrayType());
    }

    // Add test cases for other methods here...

}