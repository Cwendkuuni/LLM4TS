import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);

        assertEquals(referenceType, result);
        assertNotSame(referenceType, result);
        assertEquals(h, result._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertEquals(referenceType, result);
        assertNotSame(referenceType, result);
        assertEquals(h, result._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);

        assertEquals(referenceType, result);
        assertNotSame(referenceType, result);
        assertEquals(h, result._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);

        assertEquals(referenceType, result);
        assertNotSame(referenceType, result);
        assertEquals(h, referenceType._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();

        assertEquals(referenceType, result);
        assertNotSame(referenceType, result);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();

        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = String.class;

        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        JavaType result = referenceType._narrow(subclass);

        assertEquals(subclass, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        JavaType result = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        int count = referenceType.containedTypeCount();

        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        JavaType result = referenceType.containedType(0);

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        String name = referenceType.containedTypeName(0);

        assertEquals("T", name);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        Class<?> result = referenceType.getParameterSource();

        assertEquals(String.class, result);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>;", sb.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String result = referenceType.toString();

        assertEquals("[reference type, class java.lang.String<null>]", result);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType other = new ReferenceType(String.class, null, null, null, false);

        assertTrue(referenceType.equals(referenceType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(new Object()));
        assertTrue(referenceType.equals(other));
    }
}