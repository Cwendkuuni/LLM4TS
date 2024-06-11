import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = null;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler_SameTypeHandler_ReturnsThis() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withTypeHandler(typeHandler);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithTypeHandler_DifferentTypeHandler_ReturnsNewReferenceType() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withTypeHandler(typeHandler);

        assertNotEquals(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler_SameContentTypeHandler_ReturnsThis() {
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentTypeHandler(contentTypeHandler);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentTypeHandler_DifferentContentTypeHandler_ReturnsNewReferenceType() {
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withTypeHandler(contentTypeHandler);

        assertNotEquals(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(contentTypeHandler, result._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler_SameValueHandler_ReturnsThis() {
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withValueHandler(valueHandler);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithValueHandler_DifferentValueHandler_ReturnsNewReferenceType() {
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withValueHandler(valueHandler);

        assertNotEquals(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentValueHandler_SameContentValueHandler_ReturnsThis() {
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentValueHandler(contentValueHandler);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentValueHandler_DifferentContentValueHandler_ReturnsNewReferenceType() {
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentValueHandler(contentValueHandler);

        assertNotEquals(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(contentValueHandler, result._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping_StaticTyping_ReturnsThis() {
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withStaticTyping();

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithStaticTyping_DynamicTyping_ReturnsNewReferenceTypeWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withStaticTyping();

        assertNotEquals(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType();

        String canonicalName = referenceType.buildCanonicalName();

        assertEquals("java.lang.String<SimpleType>", canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType();

        ReferenceType result = referenceType._narrow(subclass);

        assertNotEquals(referenceType, result);
        assertEquals(subclass, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = new SimpleType();

        JavaType result = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();

        boolean result = referenceType.isReferenceType();

        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();

        int result = referenceType.containedTypeCount();

        assertEquals(1, result);
    }

    @Test
    public void testContainedType_InvalidIndex_ReturnsNull() {
        int index = 1;
        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType.containedType(index);

        assertNull(result);
    }

    @Test
    public void testContainedType_ValidIndex_ReturnsReferencedType() {
        int index = 0;
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = new SimpleType();

        JavaType result = referenceType.containedType(index);

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testContainedTypeName_InvalidIndex_ReturnsNull() {
        int index = 1;
        ReferenceType referenceType = new ReferenceType();

        String result = referenceType.containedTypeName(index);

        assertNull(result);
    }

    @Test
    public void testContainedTypeName_ValidIndex_ReturnsString() {
        int index = 0;
        ReferenceType referenceType = new ReferenceType();

        String result = referenceType.containedTypeName(index);

        assertEquals("T", result);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;

        Class<?> result = referenceType.getParameterSource();

        assertEquals(referenceType._class, result);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;

        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType();

        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;<SimpleType;>", sb.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType();

        String str = referenceType.toString();

        assertEquals("[reference type, class java.lang.String<SimpleType>]", str);
    }

    @Test
    public void testEquals_Null_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();

        boolean result = referenceType.equals(null);

        assertFalse(result);
    }

    @Test
    public void testEquals_This_ReturnsTrue() {
        ReferenceType referenceType = new ReferenceType();

        boolean result = referenceType.equals(referenceType);

        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();
        Object obj = new Object();

        boolean result = referenceType.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType other = new ReferenceType();

        boolean result = referenceType.equals(other);

        assertFalse(result);
    }

    @Test
    public void testEquals_SameClassAndReferencedType_ReturnsTrue() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType other = new ReferenceType();
        referenceType._referencedType = new SimpleType();
        other._referencedType = new SimpleType();

        boolean result = referenceType.equals(other);

        assertTrue(result);
    }

    @Test
    public void testEquals_SameClassDifferentReferencedType_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType other = new ReferenceType();
        referenceType._referencedType = new SimpleType();
        other._referencedType = new SimpleTypeWrapper();

        boolean result = referenceType.equals(other);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClassSameReferencedType_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceTypeWrapper other = new ReferenceTypeWrapper();
        referenceType._referencedType = new SimpleType();

        boolean result = referenceType.equals(other);

        assertFalse(result);
    }

    private class SimpleTypeWrapper extends SimpleType {

    }

    private class ReferenceTypeWrapper extends ReferenceType {

    }
}