import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);

        assertNotNull(referenceType);
        assertEquals(Class.class, referenceType._class);
        assertNull(referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertNull(referenceType._referencedType);
        assertNull(referenceType._anchorType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testConstructorWithArgs() {
        JavaType refType = new SimpleType(Class.class);
        JavaType anchorType = new SimpleType(Object.class);
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, refType, anchorType, null, null, false);

        assertNotNull(referenceType);
        assertEquals(Class.class, referenceType._class);
        assertNull(referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(anchorType, referenceType._anchorType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Class.class);
        JavaType refdType = new SimpleType(Object.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertNotNull(referenceType);
        assertTrue(referenceType instanceof ReferenceType);
    }

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(Class.class);
        ReferenceType referenceType = ReferenceType.construct(Class.class, null, null, null, refType);

        assertNotNull(referenceType);
        assertEquals(Class.class, referenceType._class);
        assertNull(referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._anchorType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testConstructWithRefType() {
        JavaType refType = new SimpleType(Class.class);
        ReferenceType referenceType = ReferenceType.construct(Class.class, refType);

        assertNotNull(referenceType);
        assertEquals(Class.class, referenceType._class);
        assertNotNull(referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertNull(referenceType._referencedType);
        assertEquals(refType, referenceType._anchorType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType(Object.class);
        ReferenceType result = referenceType.withContentType(contentType);

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(null);

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(null);

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType result = referenceType.withValueHandler(null);

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(null);

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();

        assertNotNull(result);
        assertEquals(referenceType, result);
    }

    @Test
    public void testRefine() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        JavaType refinedType = referenceType.refine(null, null, null, null);

        assertNotNull(refinedType);
        assertEquals(referenceType, refinedType);
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        JavaType anchorType = referenceType.getAnchorType();

        assertEquals(referenceType._anchorType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        boolean isAnchor = referenceType.isAnchorType();

        assertTrue(isAnchor);
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        JavaType contentType = referenceType.getContentType();

        assertEquals(referenceType._referencedType, contentType);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, referencedType);
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        boolean hasContentType = referenceType.hasContentType();

        assertTrue(hasContentType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        boolean isReferenceType = referenceType.isReferenceType();

        assertTrue(isReferenceType);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        String erasedSignature = referenceType.getErasedSignature(sb).toString();

        assertNotNull(erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        String genericSignature = referenceType.getGenericSignature(sb).toString();

        assertNotNull(genericSignature);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        String str = referenceType.toString();

        assertNotNull(str);
    }

    @Test
    public void testEqualsWithSameObject() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        boolean equals = referenceType.equals(referenceType);

        assertTrue(equals);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ReferenceType referenceType1 = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        boolean equals = referenceType1.equals(referenceType2);

        assertTrue(equals);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ReferenceType referenceType1 = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(TypeBindings.emptyBindings().getClass(), null, null, null, null, null, null, null, false);
        boolean equals = referenceType1.equals(referenceType2);

        assertFalse(equals);
    }

}