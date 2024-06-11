import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testNarrowBy() {
        JavaType javaType = new JavaTypeImpl();
        Class<?> subclass = Object.class;
        JavaType result = javaType.narrowBy(subclass);
        assertNotNull(result);
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType javaType = new JavaTypeImpl();
        Class<?> subclass = Object.class;
        JavaType result = javaType.forcedNarrowBy(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWidenBy() {
        JavaType javaType = new JavaTypeImpl();
        Class<?> superclass = Object.class;
        JavaType result = javaType.widenBy(superclass);
        assertNotNull(result);
    }

    @Test
    public void test_withTypeHandler() {
        JavaType javaType = new JavaTypeImpl();
        Object h = new Object();
        JavaType result = javaType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withContentTypeHandler() {
        JavaType javaType = new JavaTypeImpl();
        Object h = new Object();
        JavaType result = javaType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withValueHandler() {
        JavaType javaType = new JavaTypeImpl();
        Object h = new Object();
        JavaType result = javaType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withContentValueHandler() {
        JavaType javaType = new JavaTypeImpl();
        Object h = new Object();
        JavaType result = javaType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void test_withStaticTyping() {
        JavaType javaType = new JavaTypeImpl();
        JavaType result = javaType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void test_containedTypeOrUnknown() {
        JavaType javaType = new JavaTypeImpl();
        int index = 0;
        JavaType result = javaType.containedTypeOrUnknown(index);
        assertNotNull(result);
    }
}