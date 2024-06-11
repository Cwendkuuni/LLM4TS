import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor_withValidArguments() {
        ReferenceType type = new ReferenceType(String.class, null, null, null, null, null, null, null, false);

        assertNotNull(type);
        assertEquals(String.class, type._class);
        assertNull(type._bindings);
        assertNull(type._superClass);
        assertNull(type._superInterfaces);
        assertNull(type._referencedType);
        assertNull(type._anchorType);
        assertNull(type._valueHandler);
        assertNull(type._typeHandler);
        assertFalse(type._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullClassArgument() {
        new ReferenceType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void test_withContentType_withSameContentType() {
        JavaType contentType = new SimpleType(String.class);
        referenceType = referenceType.withContentType(contentType);

        assertSame(referenceType, referenceType.withContentType(contentType));
        assertSame(contentType, referenceType.getContentType());
    }

    @Test
    public void test_withContentType_withDifferentContentType() {
        JavaType contentType1 = new SimpleType(String.class);
        JavaType contentType2 = new SimpleType(Integer.class);
        referenceType = referenceType.withContentType(contentType1);

        assertNotSame(referenceType, referenceType.withContentType(contentType2));
        assertSame(contentType2, referenceType.getContentType());
    }

    @Test
    public void test_withTypeHandler_withSameTypeHandler() {
        Object typeHandler = new Object();
        referenceType = referenceType.withTypeHandler(typeHandler);

        assertSame(referenceType, referenceType.withTypeHandler(typeHandler));
        assertSame(typeHandler, referenceType._typeHandler);
    }

    @Test
    public void test_withTypeHandler_withDifferentTypeHandler() {
        Object typeHandler1 = new Object();
        Object typeHandler2 = new Object();
        referenceType = referenceType.withTypeHandler(typeHandler1);

        assertNotSame(referenceType, referenceType.withTypeHandler(typeHandler2));
        assertSame(typeHandler2, referenceType._typeHandler);
    }

    @Test
    public void test_withContentTypeHandler_withSameContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = new SimpleType(String.class);
        contentType = contentType.withTypeHandler(typeHandler);
        referenceType = referenceType.withContentType(contentType);

        assertSame(referenceType, referenceType.withContentTypeHandler(typeHandler));
        assertSame(typeHandler, referenceType.getContentType().getTypeHandler());
    }

    @Test
    public void test_withContentTypeHandler_withDifferentContentTypeHandler() {
        Object typeHandler1 = new Object();
        Object typeHandler2 = new Object();
        JavaType contentType = new SimpleType(String.class);
        contentType = contentType.withTypeHandler(typeHandler1);
        referenceType = referenceType.withContentType(contentType);

        assertNotSame(referenceType, referenceType.withContentTypeHandler(typeHandler2));
        assertSame(typeHandler2, referenceType.getContentType().getTypeHandler());
    }

    @Test
    public void test_withValueHandler_withSameValueHandler() {
        Object valueHandler = new Object();
        referenceType = referenceType.withValueHandler(valueHandler);

        assertSame(referenceType, referenceType.withValueHandler(valueHandler));
        assertSame(valueHandler, referenceType._valueHandler);
    }

    @Test
    public void test_withValueHandler_withDifferentValueHandler() {
        Object valueHandler1 = new Object();
        Object valueHandler2 = new Object();
        referenceType = referenceType.withValueHandler(valueHandler1);

        assertNotSame(referenceType, referenceType.withValueHandler(valueHandler2));
        assertSame(valueHandler2, referenceType._valueHandler);
    }

    @Test
    public void test_withContentValueHandler_withSameContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = new SimpleType(String.class);
        contentType = contentType.withValueHandler(valueHandler);
        referenceType = referenceType.withContentType(contentType);

        assertSame(referenceType, referenceType.withContentValueHandler(valueHandler));
        assertSame(valueHandler, referenceType.getContentType().getValueHandler());
    }

    @Test
    public void test_withContentValueHandler_withDifferentContentValueHandler() {
        Object valueHandler1 = new Object();
        Object valueHandler2 = new Object();
        JavaType contentType = new SimpleType(String.class);
        contentType = contentType.withValueHandler(valueHandler1);
        referenceType = referenceType.withContentType(contentType);

        assertNotSame(referenceType, referenceType.withContentValueHandler(valueHandler2));
        assertSame(valueHandler2, referenceType.getContentType().getValueHandler());
    }

    @Test
    public void test_withStaticTyping_withStaticTyping() {
        referenceType = referenceType.withStaticTyping();

        assertSame(referenceType, referenceType.withStaticTyping());
        assertTrue(referenceType._asStatic);
    }

    @Test
    public void test_withStaticTyping_withoutStaticTyping() {
        referenceType._asStatic = true;
        referenceType = referenceType.withStaticTyping();

        assertSame(referenceType, referenceType.withStaticTyping());
        assertTrue(referenceType._asStatic);
    }

    @Test
    public void test_refine() {
        Class<?> rawType = String.class;
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = { new SimpleType(CharSequence.class) };
        JavaType referredType = new SimpleType(Integer.class);
        referenceType = (ReferenceType) referenceType.refine(rawType, null, superClass, superInterfaces);
        referenceType = (ReferenceType) referenceType.withContentType(referredType);

        assertEquals(rawType, referenceType._class);
        assertNull(referenceType._bindings);
        assertSame(superClass, referenceType._superClass);
        assertArrayEquals(superInterfaces, referenceType._superInterfaces);
        assertSame(referredType, referenceType.getContentType());
        assertFalse(referenceType.getContentType().isReferenceType());
    }

    @Test
    public void test_getErasedSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType(Integer.class);

        StringBuilder expected = sb.append(String.class.getName()).append('<').append(Integer.class.getCanonicalName());

        assertEquals(expected.toString(), referenceType.getErasedSignature(sb).toString());
    }

    @Test
    public void test_getGenericSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType._class = String.class;
        referenceType._referencedType = new SimpleType(Integer.class);

        StringBuilder expected = sb.append(String.class.getName()).append('<').append(Integer.class.getCanonicalName()).append(">;");

        assertEquals(expected.toString(), referenceType.getGenericSignature(sb).toString());
    }

    @Test
    public void test_getAnchorType_withAnchorType() {
        JavaType anchorType = new SimpleType(String.class);
        referenceType._anchorType = anchorType;

        assertSame(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void test_getAnchorType_withoutAnchorType() {
        referenceType._anchorType = null;

        assertSame(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void test_isAnchorType_withAnchorType() {
        referenceType._anchorType = referenceType;

        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void test_isAnchorType_withoutAnchorType() {
        referenceType._anchorType = new SimpleType(String.class);

        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void test_toString() {
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<null>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void test_equals_withSameReferenceType() {
        ReferenceType other = referenceType;

        assertTrue(referenceType.equals(other));
    }

    @Test
    public void test_equals_withEqualReferenceTypes() {
        ReferenceType other = new ReferenceType(null, null, null, null, null, null, null, null, false);

        assertTrue(referenceType.equals(other));
    }

    @Test
    public void test_equals_withDifferentReferenceTypes() {
        ReferenceType other = new ReferenceType(String.class, null, null, null, null, null, null, null, false);

        assertFalse(referenceType.equals(other));
    }

    @Test
    public void test_equals_withDifferentClass() {
        ReferenceType other = new ReferenceType(null, null, null, null, null, null, null, null, true);

        assertFalse(referenceType.equals(other));
    }

    @Test
    public void test_equals_withNull() {
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void test_equals_withDifferentObjectType() {
        assertFalse(referenceType.equals(new Object()));
    }
}