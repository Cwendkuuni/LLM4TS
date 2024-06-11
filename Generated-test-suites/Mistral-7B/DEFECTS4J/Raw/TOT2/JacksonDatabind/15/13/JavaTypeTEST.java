import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withTypeHandler(handler);
        assertNotNull(javaTypeWithHandler);
        assertEquals(String.class, javaTypeWithHandler.getRawClass());
        assertEquals(handler, javaTypeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWithIncompatibleTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withTypeHandler(handler);
        JavaType javaTypeWithStringHandler = javaTypeWithHandler.withTypeHandler(String.class);
        fail("Expected IllegalArgumentException but was able to create JavaType instance with incompatible type handler");
    }

    @Test
    public void testJavaTypeIsInterface() {
        Class<JavaType> javaTypeClass = JavaType.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(javaTypeClass);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType narrowedJavaType = javaType.narrowBy(Integer[].class);
        assertNotNull(narrowedJavaType);
        assertTrue(Integer[].class.isAssignableFrom(narrowedJavaType.getRawClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalidType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType narrowedJavaType = javaType.narrowBy(String.class);
        fail("Expected IllegalArgumentException but was able to create JavaType instance with incompatible type");
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType widenedJavaType = javaType.widenBy(Number.class);
        assertNotNull(widenedJavaType);
        assertTrue(Number.class.isAssignableFrom(widenedJavaType.getRawClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalidType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType widenedJavaType = javaType.widenBy(String.class);
        fail("Expected IllegalArgumentException but was able to create JavaType instance with incompatible type");
    }
}