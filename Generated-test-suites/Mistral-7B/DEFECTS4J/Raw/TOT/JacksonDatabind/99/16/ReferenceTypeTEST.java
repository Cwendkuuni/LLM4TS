import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refdType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        // Check type properties
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(int[].class, baseType.getRawClass());
        assertEquals(String.class, refdType.getRawClass());
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertFalse(referenceType.isStatic());

        // Check behavior
        assertTrue(referenceType.isReferenceType());
        assertFalse(baseType.isReferenceType());
        assertFalse(refdType.isReferenceType());
        assertFalse(referenceType.hasContentType());
        assertEquals(refdType, referenceType.getReferencedType());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInterfaces, refType);

        // Check type properties
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertArrayEquals(superInterfaces, referenceType.getSuperInterfaces());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertFalse(referenceType.isStatic());

        // Check behavior
        assertTrue(referenceType.isReferenceType());
        assertFalse(cls.isInstance(referenceType));
        assertFalse(refType.isInstance(referenceType));
        assertTrue(referenceType.hasContentType());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);
        JavaType contentType = TypeFactory.defaultInstance().constructType(long.class);

        // Check initial state
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertFalse(referenceType.hasContentType());

        // Test withContentType
        ReferenceType newReferenceType = referenceType.withContentType(contentType);

        // Check updated state
        assertEquals(int[].class, newReferenceType.getRawClass());
        assertEquals(String.class, newReferenceType.getReferencedType().getRawClass());
        assertTrue(newReferenceType.hasContentType());
        assertEquals(contentType, newReferenceType.getContentType());

        // Check identity
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        // Check initial state
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertNull(referenceType.getTypeHandler());

        // Test withTypeHandler
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

        // Check updated state
        assertEquals(int[].class, newReferenceType.getRawClass());
        assertEquals(String.class, newReferenceType.getReferencedType().getRawClass());
        assertSame(typeHandler, newReferenceType.getTypeHandler());

        // Check identity
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentTypeHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        // Check initial state
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertNull(referenceType.getReferencedType().getValueHandler());
        assertNull(referenceType.getTypeHandler());

        // Test withContentTypeHandler and withTypeHandler
        JavaType newContentType = TypeFactory.defaultInstance().constructType(long.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler)
                .withTypeHandler(typeHandler);

        // Check updated state
        assertEquals(int[].class, newReferenceType.getRawClass());
        assertEquals(String.class, newReferenceType.getReferencedType().getRawClass());
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());

        // Check identity
        assertNotSame(referenceType, newReferenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRefdType() {
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInterfaces, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefdType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refdType = null;

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
    }
}