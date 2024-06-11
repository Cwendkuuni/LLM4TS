import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType result = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, result._class);
        assertEquals(refType, result._referencedType);
        assertEquals(null, result._valueHandler);
        assertEquals(null, result._typeHandler);
        assertEquals(false, result._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withTypeHandler(h);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withValueHandler(h);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withContentValueHandler(h);

        assertEquals(referenceType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();

        ReferenceType result = referenceType.withStaticTyping();

        assertEquals(referenceType, result);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType();

        String result = referenceType.buildCanonicalName();

        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType", result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = Object.class;
        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType._narrow(subclass);

        assertEquals(new ReferenceType(subclass, referenceType._referencedType, referenceType._valueHandler, referenceType._typeHandler, referenceType._asStatic), result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();

        boolean result = referenceType.isReferenceType();

        assertEquals(true, result);
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();

        int result = referenceType.containedTypeCount();

        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        int index = 0;
        ReferenceType referenceType = new ReferenceType();

        JavaType result = referenceType.containedType(index);

        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testContainedTypeName() {
        int index = 0;
        ReferenceType referenceType = new ReferenceType();

        String result = referenceType.containedTypeName(index);

        assertEquals("T", result);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType();

        Class<?> result = referenceType.getParameterSource();

        assertEquals(referenceType._class, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();

        StringBuilder result = referenceType.getErasedSignature(sb);

        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();

        StringBuilder result = referenceType.getGenericSignature(sb);

        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<;", result.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();

        String result = referenceType.toString();

        assertEquals("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<T>]", result);
    }

    @Test
    public void testEquals() {
        Object o = new ReferenceType();
        ReferenceType referenceType = new ReferenceType();

        boolean result = referenceType.equals(o);

        assertEquals(true, result);
    }
}