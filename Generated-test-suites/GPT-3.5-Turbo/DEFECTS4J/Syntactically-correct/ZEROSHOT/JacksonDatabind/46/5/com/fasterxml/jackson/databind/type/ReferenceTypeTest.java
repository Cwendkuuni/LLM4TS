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
        Object h = null;

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);

        assertSame(referenceType, result);

        Object newTypeHandler = new Object();
        referenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.withTypeHandler(newTypeHandler);

        assertNotSame(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(newTypeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertSame(referenceType, result);

        Object newContentTypeHandler = new Object();
        referenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.withContentTypeHandler(newContentTypeHandler);

        assertNotSame(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withTypeHandler(newContentTypeHandler), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);

        assertSame(referenceType, result);

        Object newValueHandler = new Object();
        referenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.withValueHandler(newValueHandler);

        assertNotSame(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(newValueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);

        assertSame(referenceType, result);

        Object newContentValueHandler = new Object();
        referenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.withContentValueHandler(newContentValueHandler);

        assertNotSame(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withValueHandler(newContentValueHandler), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();

        assertSame(referenceType, result);

        referenceType = new ReferenceType(Object.class, null, null, null, true);
        result = referenceType.withStaticTyping();

        assertSame(referenceType, result);

        referenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.withStaticTyping();

        assertNotSame(referenceType, result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withStaticTyping(), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();

        assertEquals(Object.class.getName() + "<" + null + ">", canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType result = referenceType._narrow(subclass);

        assertNotSame(referenceType, result);
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
        boolean result = referenceType.isReferenceType();

        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        int count = referenceType.containedTypeCount();

        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        JavaType refType = null;

        ReferenceType referenceType = new ReferenceType(Object.class, refType, null, null, false);
        JavaType result = referenceType.containedType(0);

        assertEquals(refType, result);

        result = referenceType.containedType(1);

        assertNull(result);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String typeName = referenceType.containedTypeName(0);

        assertEquals("T", typeName);

        typeName = referenceType.containedTypeName(1);

        assertNull(typeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        Class<?> source = referenceType.getParameterSource();

        assertEquals(Object.class, source);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);

        assertSame(sb, result);

        String expectedSignature = Object.class.getName();
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);

        assertSame(sb, result);

        String expectedSignature = Object.class.getName() + "<" + null + ";" ;
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        String str = referenceType.toString();

        String expectedStr = "[reference type, class " + Object.class.getName() + "<" + null + ">]>";

        assertEquals(expectedStr, str);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, false);
        boolean result = referenceType.equals(null);

        assertFalse(result);

        result = referenceType.equals(new Object());

        assertFalse(result);

        result = referenceType.equals(referenceType);

        assertTrue(result);

        ReferenceType anotherReferenceType = new ReferenceType(Object.class, null, null, null, false);
        result = referenceType.equals(anotherReferenceType);

        assertTrue(result);

        anotherReferenceType = new ReferenceType(String.class, null, null, null, false);
        result = referenceType.equals(anotherReferenceType);

        assertFalse(result);

        anotherReferenceType = new ReferenceType(Object.class, referenceType._referencedType, null, null, false);
        result = referenceType.equals(anotherReferenceType);

        assertFalse(result);
    }
}