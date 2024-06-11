import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = ReferenceType.construct(clazz, referencedType, null);

        assertEquals(clazz, referenceType.getRawClass());
        assertEquals(referencedType.getClass(), referenceType.getContentType().getClass());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithTypeHandlers() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        // Test withTypeHandler
        ReferenceType withTypeHandler = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());

        // Test withContentTypeHandler
        JavaType newReferencedType = new ClassType(long.class);
        ReferenceType withContentTypeHandler = withTypeHandler.withContentTypeHandler(newReferencedType);
        assertNotSame(withTypeHandler, withContentTypeHandler);
        assertEquals(newReferencedType, withContentTypeHandler.getContentType());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        // Test withValueHandler
        ReferenceType withValueHandler = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());

        // Test withContentValueHandler
        Object contentValueHandler = new Object();
        ReferenceType withContentValueHandler = withValueHandler.withContentValueHandler(contentValueHandler);
        assertNotSame(withValueHandler, withContentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        Class<?> subclass = Object.class;
        ReferenceType narrow = referenceType._narrow(subclass);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testGetReferencedType() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        JavaType containedType = referenceType.containedType(0);
        assertEquals(referencedType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        assertEquals(clazz, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> clazz = String.class;
        JavaType referencedType = new ClassType(int.class);
        ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", sb.toString());
    }
}