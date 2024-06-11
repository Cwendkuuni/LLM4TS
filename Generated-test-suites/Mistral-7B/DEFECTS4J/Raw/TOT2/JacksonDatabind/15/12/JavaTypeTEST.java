import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertEquals("Expected JavaType instance", javaType.getClass(), JavaType.class);
        assertEquals("Expected String class", javaType.getRawClass(), stringClass);
    }

    @Test
    public void testIsConcreteMethod() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertTrue("Expected JavaType to be concrete", javaType.isConcrete());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithInvalidClass() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        javaType.narrowBy(stringClass);
    }

    @Test
    public void testNarrowByWithSameClass() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType narrowedType = javaType.narrowBy(stringClass);
        assertEquals("Expected same JavaType instance", narrowedType, javaType);
    }

    @Test
    public void testNarrowByWithSubclass() throws Exception {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType narrowedType = javaType.narrowBy(stringBuilderClass);
        assertNotSame("Expected different JavaType instances", javaType, narrowedType);
        assertTrue("Expected StringBuilder class", narrowedType.getRawClass().isAssignableFrom(JavaType.class));
    }

    @Test
    public void testWidenByWithSuperclass() throws Exception {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType widenedType = javaType.widenBy(objectClass);
        assertEquals("Expected same JavaType instance", widenedType, javaType);
    }

    @Test
    public void testWidenByWithSameClass() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType widenedType = javaType.widenBy(stringClass);
        assertEquals("Expected same JavaType instance", widenedType, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByWithInvalidClass() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        javaType.widenBy(stringClass);
    }

    @Test
    public void testHasRawClassMethod() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertTrue("Expected JavaType to have raw class", javaType.hasRawClass(stringClass));
    }

    @Test
    public void testIsInterfaceMethod() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertFalse("Expected JavaType not to be interface", javaType.isInterface());
    }

    @Test
    public void testIsMapLikeTypeMethod() throws Exception {
        Class<java.util.HashMap<String, Integer>> mapClass = HashMap.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(mapClass);
        assertTrue("Expected JavaType to be map-like type", javaType.isMapLikeType());
    }

    @Test
    public void testIsContainerTypeMethod() throws Exception {
        Class<java.util.ArrayList<String>> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        assertTrue("Expected JavaType to be container type", javaType.isContainerType());
    }

    @Test(expected = AssertionError.class)
    public void testIsContainerTypeMethodWithNonContainerType() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        javaType.isContainerType();
    }
}