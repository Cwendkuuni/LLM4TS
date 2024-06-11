import org.junit.*;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class cls = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(cls, referenceType._class);
        assertEquals(bindings, referenceType._bindings);
        assertEquals(superClass, referenceType._superClass);
        assertEquals(superInts, referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(anchorType, referenceType._anchorType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertEquals(asStatic, referenceType._asStatic);
    }

    @Test
    public void testConstructor() {
        TypeBase base = null;
        JavaType refType = null;

        ReferenceType referenceType = new ReferenceType(base, refType);

        assertEquals(base, referenceType._base);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(referenceType, referenceType._anchorType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType refdType = null;

        try {
            ReferenceType.upgradeFrom(baseType, refdType);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }
    }

    @Test
    public void testConstruct2() {
        Class cls = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        assertEquals(cls, referenceType._class);
        assertEquals(bindings, referenceType._bindings);
        assertEquals(superClass, referenceType._superClass);
        assertEquals(superInts, referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._anchorType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testConstruct3() {
        Class cls = Object.class;
        JavaType refType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, refType);

        assertEquals(cls, referenceType._class);
        assertEquals(TypeBindings.emptyBindings(), referenceType._bindings);
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
        JavaType contentType = null;

        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.withContentType(contentType);

        assertEquals(contentType, result._referencedType);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(referenceType._superClass, result._superClass);
        assertEquals(referenceType._superInterfaces, result._superInterfaces);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withTypeHandler(h);

        assertEquals(h, result._typeHandler);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(referenceType._superClass, result._superClass);
        assertEquals(referenceType._superInterfaces, result._superInterfaces);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(referenceType._superClass, result._superClass);
        assertEquals(referenceType._superInterfaces, result._superInterfaces);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(h, result._referencedType._typeHandler);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler2() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(referenceType._superClass, result._superClass);
        assertEquals(referenceType._superInterfaces, result._superInterfaces);
        assertNotEquals(referenceType._referencedType, result._referencedType);
        assertEquals(h, result._referencedType._typeHandler);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withStaticTyping();

        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(referenceType._superClass, result._superClass);
        assertEquals(referenceType._superInterfaces, result._superInterfaces);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testRefine() {
        Class rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, result._class);
        assertEquals(referenceType._bindings, result._bindings);
        assertEquals(superClass, result._superClass);
        assertEquals(superInterfaces, result._superInterfaces);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._anchorType, result._anchorType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.getContentType();

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType();
        boolean result = referenceType.hasContentType();

        assertTrue(result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        boolean result = referenceType.isReferenceType();

        assertTrue(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = new ReferenceType();
        StringBuilder result = referenceType.getErasedSignature(sb);

        assertEquals("Ljava/lang/Object;", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = new ReferenceType();
        StringBuilder result = referenceType.getGenericSignature(sb);

        assertEquals("Ljava/lang/Object<>;", result.toString());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.getAnchorType();

        assertEquals(referenceType, result);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        boolean result = referenceType.isAnchorType();

        assertTrue(result);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        String result = referenceType.toString();

        assertEquals("[reference type, class java.lang.Object<>]", result);
    }

    @Test
    public void testEquals() {
        Object o = null;

        ReferenceType referenceType = new ReferenceType();
        boolean result = referenceType.equals(o);

        assertFalse(result);
    }
}