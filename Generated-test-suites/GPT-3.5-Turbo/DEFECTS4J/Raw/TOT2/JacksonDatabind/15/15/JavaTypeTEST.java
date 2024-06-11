import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testWithNullTypeHandler() {
        JavaType javaType = new ConcreteJavaType(Object.class, 0, null, new Object(), false);
        JavaType result = javaType.withTypeHandler(null);
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithNonNullTypeHandler() {
        JavaType javaType = new ConcreteJavaType(Object.class, 0, null, new Object(), false);
        Object newHandler = new Object();
        JavaType result = javaType.withTypeHandler(newHandler);
        assertEquals(newHandler, result.getTypeHandler());
    }

    @Test
    public void testNarrowBySameClass() {
        JavaType javaType = new ConcreteJavaType(String.class, 0, null, null, false);
        JavaType result = javaType.narrowBy(String.class);
        assertSame(javaType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByIncompatibleClass() {
        JavaType javaType = new ConcreteJavaType(String.class, 0, null, null, false);
        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testForcedNarrowBySameClass() {
        JavaType javaType = new ConcreteJavaType(Integer.class, 0, null, null, false);
        JavaType result = javaType.forcedNarrowBy(Integer.class);
        assertSame(javaType, result);
    }

    @Test
    public void testWidenByInheritedClass() {
        JavaType javaType = new ConcreteJavaType(Number.class, 0, null, null, false);
        JavaType result = javaType.widenBy(Integer.class);
        assertEquals(Integer.class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByNonInheritedClass() {
        JavaType javaType = new ConcreteJavaType(Long.class, 0, null, null, false);
        javaType.widenBy(Integer.class);
    }

    // Additional test cases can be added here for other methods

}