import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType newJavaType = javaType.withTypeHandler(new Object());
        assertNotSame(javaType, newJavaType);
        assertEquals(newJavaType.getClass(), javaType.getClass());
    }

    @Test
    public void testNarrowBy() {
        Class<String> rawClassString = String.class;
        Class<Integer> rawClassInteger = Integer.class;
        JavaType javaTypeString = new JavaType(rawClassString, 0, null, null, false);
        JavaType javaTypeInteger = javaTypeString.narrowBy(rawClassInteger);
        assertEquals(Integer.class, javaTypeInteger.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithInvalidSubclass() {
        Class<String> rawClassString = String.class;
        Class<Boolean> rawClassBoolean = Boolean.class;
        JavaType javaTypeString = new JavaType(rawClassString, 0, null, null, false);
        JavaType javaTypeBoolean = javaTypeString.narrowBy(rawClassBoolean);
    }

    @Test
    public void testWidenBy() {
        Class<String> rawClassString = String.class;
        Class<Object> rawClassObject = Object.class;
        JavaType javaTypeString = new JavaType(rawClassString, 0, null, null, false);
        JavaType javaTypeObject = javaTypeString.widenBy(rawClassObject);
        assertEquals(Object.class, javaTypeObject.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByWithInvalidSuperclass() {
        Class<String> rawClassString = String.class;
        Class<Integer> rawClassInteger = Integer.class;
        JavaType javaTypeString = new JavaType(rawClassString, 0, null, null, false);
        JavaType javaTypeInteger = javaTypeString.widenBy(rawClassInteger);
    }
}