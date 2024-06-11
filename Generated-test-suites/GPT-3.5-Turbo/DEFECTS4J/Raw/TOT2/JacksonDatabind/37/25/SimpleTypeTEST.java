import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    // Test case for public static SimpleType constructUnsafe(Class<?> raw)
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    // Test case for public static SimpleType construct(Class<?> cls)
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
    }

    // Test case for protected JavaType _narrow(Class<?> subclass)
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType._narrow(Object.class);
        assertNotEquals(simpleType, javaType);
    }

    // Test case for public JavaType withContentType(JavaType contentType)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(null);
    }

    // Test case for public SimpleType withTypeHandler(Object h)
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withTypeHandler("handler");
        assertNotEquals(simpleType, newSimpleType);
    }

    // Test case for public JavaType withContentTypeHandler(Object h)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler("handler");
    }

    // Test case for public SimpleType withValueHandler(Object h)
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withValueHandler("handler");
        assertNotEquals(simpleType, newSimpleType);
    }

    // Test case for public SimpleType withContentValueHandler(Object h)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler("handler");
    }

    // Test case for public SimpleType withStaticTyping()
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertNotEquals(simpleType, newSimpleType);
    }

    // Test case for public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(javaType);
    }

    // Test case for public boolean isContainerType()
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

}