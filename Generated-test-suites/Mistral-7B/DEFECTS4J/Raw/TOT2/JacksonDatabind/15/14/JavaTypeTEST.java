import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals(rawClass, javaType.getRawClass());
        assertEquals(0, javaType.hashCode());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStaticTyping());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withTypeHandler(handler);
        assertSame(javaType, javaTypeWithHandler);
        assertEquals(handler, javaTypeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidTypeHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Object handler = new int[0];
        JavaType javaTypeWithHandler = javaType.withTypeHandler(handler);
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withValueHandler(handler);
        assertSame(javaType, javaTypeWithHandler);
        assertEquals(handler, javaTypeWithHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidValueHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Object handler = new int[0];
        JavaType javaTypeWithHandler = javaType.withValueHandler(handler);
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.isStaticTyping());
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<?> rawClass = String[].class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType javaTypeNarrowed = javaType.narrowBy(String.class);
        assertTrue(javaTypeNarrowed instanceof JavaType.ArrayType);
        assertEquals(String[].class, javaTypeNarrowed.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalidType() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Class<?> invalidType = int.class;
        JavaType javaTypeNarrowed = javaType.narrowBy(invalidType);
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType javaTypeWidened = javaType.widenBy(Object[].class);
        assertTrue(javaTypeWidened instanceof JavaType.ArrayType);
        assertEquals(Object[].class, javaTypeWidened.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalidType() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Class<?> invalidType = int.class;
        JavaType javaTypeWidened = javaType.widenBy(invalidType);
    }

    @Test
    public void testJavaTypeNarrowContentsBy() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType javaTypeNarrowed = javaType.narrowContentsBy(String.class);
        assertFalse(javaTypeNarrowed instanceof JavaType.ArrayType);
        assertEquals(List.class, javaTypeNarrowed.getRawClass());
        assertEquals(String.class, javaTypeNarrowed.getContainedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowContentsByInvalidType() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Class<?> invalidType = int[].class;
        JavaType javaTypeNarrowed = javaType.narrowContentsBy(invalidType);
    }

    @Test
    public void testJavaTypeWidenContentsBy() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType javaTypeWidened = javaType.widenContentsBy(Object.class);
        assertFalse(javaTypeWidened instanceof JavaType.ArrayType);
        assertEquals(List.class, javaTypeWidened.getRawClass());
        assertEquals(Object.class, javaTypeWidened.getContainedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenContentsByInvalidType() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Class<?> invalidType = int[].class;
        JavaType javaTypeWidened = javaType.widenContentsBy(invalidType);
    }
}