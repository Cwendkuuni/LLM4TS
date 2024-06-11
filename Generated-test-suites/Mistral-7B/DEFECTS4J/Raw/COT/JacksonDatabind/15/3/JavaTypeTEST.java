import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<?> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        assertEquals(rawClass, javaType.getRawClass());
        assertNotNull(javaType._class);
        assertNotNull(javaType._hash);
        assertNotNull(javaType._valueHandler);
        assertNotNull(javaType._typeHandler);
        assertEquals(false, javaType._asStatic);
    }
}