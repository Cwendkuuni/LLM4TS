import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
    }

    @Test
    public void testJavaTypeWithHandlers() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        javaType = javaType.withValueHandler(new Object() {});
        javaType = javaType.withTypeHandler(new Object() {});
        assertEquals("class [I", javaType.getErasedSignature());
        assertEquals(int[].class, javaType.getRawClass());
        assertFalse(javaType.isConcrete()); // since it's an array type
    }

    @Test
    public void testJavaTypeNarrowing() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowedType = javaType.narrowBy(StringBuffer.class);
        assertEquals("class java.lang.String", narrowedType.getErasedSignature());
        assertEquals(StringBuffer.class, narrowedType.getRawClass());
        assertTrue(narrowedType.isAssignableFrom(javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidNarrowing() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowedType = javaType.narrowBy(Integer.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testJavaTypeWidening() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(StringBuffer.class);
        JavaType widenedType = javaType.widenBy(String.class);
        assertEquals("class java.lang.StringBuffer", widenedType.getErasedSignature());
        assertEquals(StringBuffer.class, widenedType.getRawClass());
        assertTrue(widenedType.isAssignableFrom(javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidWidening() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType widenedType = javaType.widenBy(String.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testJavaTypeWithRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType withRawClass = javaType.withRawClass(List.class);
        assertEquals("class java.util.ArrayList", withRawClass.getErasedSignature());
        assertEquals(List.class, withRawClass.getRawClass());
        assertTrue(withRawClass.hasRawClass(List.class));
    }
}