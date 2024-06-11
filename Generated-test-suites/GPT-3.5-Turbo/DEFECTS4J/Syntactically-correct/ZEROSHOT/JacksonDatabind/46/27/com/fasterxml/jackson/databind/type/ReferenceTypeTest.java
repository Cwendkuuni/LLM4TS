import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(h, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(h, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(h, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        String expected = "com.fasterxml.jackson.databind.type.ReferenceType<Object>";
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String result = referenceType.buildCanonicalName();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        JavaType result = referenceType._narrow(subclass);
        assertNotNull(result);
        assertEquals(subclass, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = null;
        ReferenceType referenceType = new ReferenceType(Object.class, refType, null, null, false);
        JavaType result = referenceType.getReferencedType();
        assertEquals(refType, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        int result = referenceType.containedTypeCount();
        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        JavaType result = referenceType.containedType(0);
        assertNotNull(result);
        assertEquals(referenceType._referencedType, result);
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String result = referenceType.containedTypeName(0);
        assertNotNull(result);
        assertEquals("T", result);
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        Class<?> result = referenceType.getParameterSource();
        assertEquals(referenceType._class, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String result = referenceType.toString();
        assertNotNull(result);
        assertTrue(result.contains("reference type"));
        assertTrue(result.contains("class"));
        assertTrue(result.contains("com.fasterxml.jackson.databind.type.ReferenceType<Object>"));
        assertTrue(result.contains("Object"));
    }

    @Test
    public void testEquals() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object o = null;
        assertFalse(referenceType.equals(o));
        assertFalse(referenceType.equals(new Object()));
        assertTrue(referenceType.equals(referenceType));

        ReferenceType other = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertTrue(referenceType.equals(other));
    }
}