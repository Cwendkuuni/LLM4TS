import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler1 = new Object();
        Object typeHandler2 = new Object();

        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, typeHandler1, true);
        ReferenceType modifiedReferenceType = referenceType.withTypeHandler(typeHandler2);

        assertEquals(String.class, modifiedReferenceType._class);
        assertEquals(typeHandler2, modifiedReferenceType._typeHandler);
        assertEquals(referenceType._referencedType, modifiedReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, modifiedReferenceType._valueHandler);
        assertTrue(modifiedReferenceType._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, typeHandler, false);
        ReferenceType modifiedReferenceType = referenceType.withContentTypeHandler(typeHandler);

        assertEquals(String.class, modifiedReferenceType._class);
        assertEquals(referenceType._typeHandler, modifiedReferenceType._typeHandler);
        assertEquals(referenceType._referencedType, modifiedReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, modifiedReferenceType._valueHandler);
        assertTrue(modifiedReferenceType._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler1 = new Object();
        Object valueHandler2 = new Object();

        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), valueHandler1, null, true);
        ReferenceType modifiedReferenceType = referenceType.withValueHandler(valueHandler2);

        assertEquals(String.class, modifiedReferenceType._class);
        assertEquals(valueHandler2, modifiedReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, modifiedReferenceType._typeHandler);
        assertEquals(referenceType._referencedType, modifiedReferenceType._referencedType);
        assertTrue(modifiedReferenceType._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), valueHandler, null, false);
        ReferenceType modifiedReferenceType = referenceType.withContentValueHandler(valueHandler);

        assertEquals(String.class, modifiedReferenceType._class);
        assertEquals(referenceType._valueHandler, modifiedReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, modifiedReferenceType._typeHandler);
        assertEquals(referenceType._referencedType, modifiedReferenceType._referencedType);
        assertTrue(modifiedReferenceType._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);
        ReferenceType modifiedReferenceType = referenceType.withStaticTyping();

        assertEquals(String.class, modifiedReferenceType._class);
        assertEquals(referenceType._referencedType, modifiedReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, modifiedReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, modifiedReferenceType._typeHandler);
        assertTrue(modifiedReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, true);
        String expected = "com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.SimpleType>";
        assertEquals(expected, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);
        Class<?> subclass = StringBuilder.class;
        ReferenceType narrowedReferenceType = referenceType._narrow(subclass);

        assertEquals(subclass, narrowedReferenceType._class);
        assertEquals(referenceType._referencedType, narrowedReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, narrowedReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, narrowedReferenceType._typeHandler);
        assertFalse(narrowedReferenceType._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, true);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        // ReferenceType is always a reference type
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, true);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, true);

        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);

        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, true);
        String expected = "com.fasterxml.jackson.databind.type.ReferenceType";

        assertEquals(expected, referenceType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);
        String expected = "com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.SimpleType>";

        assertEquals(expected, referenceType.getGenericSignature(sb).toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, true);
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.SimpleType>]";

        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = new SimpleType();

        ReferenceType referenceType1 = new ReferenceType(String.class, refType, null, null, true);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType, new Object(), new Object(), true);

        assertTrue(referenceType1.equals(referenceType2));
    }
}