import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(String.class, JavaType.ReferenceType(String.class),
                null, null);
    }

    @Test
    public void testWithEmptyHandlers() {
        Object typeHandler = referenceType.getTypeHandler();
        Object valueHandler = referenceType.getValueHandler();
        Object contentValueHandler = referenceType.getContentValueHandler();
        Object contentTypeHandler = referenceType.getContentTypeHandler();

        ReferenceType result = referenceType.withTypeHandler(typeHandler);

        assertSame(referenceType, result);
        assertSame(typeHandler, result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = referenceType.withValueHandler(valueHandler);

        assertSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertSame(valueHandler, result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = referenceType.withContentTypeHandler(contentTypeHandler);

        assertSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertSame(contentTypeHandler, result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = referenceType.withContentValueHandler(contentValueHandler);

        assertSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertSame(contentValueHandler, result.getContentValueHandler());

    }

    @Test
    public void testWithNonEmptyHandlers() {
        Object typeHandler1 = new Object();
        Object typeHandler2 = new Object();
        Object valueHandler1 = new Object();
        Object valueHandler2 = new Object();
        Object contentTypeHandler1 = new Object();
        Object contentTypeHandler2 = new Object();
        Object contentValueHandler1 = new Object();
        Object contentValueHandler2 = new Object();

        ReferenceType result = referenceType.withTypeHandler(typeHandler1);

        assertNotSame(referenceType, result);
        assertSame(typeHandler1, result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withTypeHandler(typeHandler2);

        assertNotSame(referenceType, result);
        assertNotSame(result, result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withValueHandler(valueHandler1);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertSame(valueHandler1, result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withValueHandler(valueHandler2);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNotSame(valueHandler1, result.getValueHandler());
        assertNotSame(result, result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withContentTypeHandler(contentTypeHandler1);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertSame(contentTypeHandler1, result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withContentTypeHandler(contentTypeHandler2);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNotSame(contentTypeHandler1, result.getContentTypeHandler());
        assertNotSame(result, result.getContentTypeHandler());
        assertNull(result.getContentValueHandler());

        result = result.withContentValueHandler(contentValueHandler1);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertSame(contentValueHandler1, result.getContentValueHandler());

        result = result.withContentValueHandler(contentValueHandler2);

        assertNotSame(referenceType, result);
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());
        assertNull(result.getContentTypeHandler());
        assertNotSame(contentValueHandler1, result.getContentValueHandler());
        assertNotSame(result, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withTypeHandler(new Object());

        assertFalse(result.isStaticTyping());

        result = referenceType.withStaticTyping();

        assertNotSame(referenceType, result);
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        String canonicalName = referenceType.buildCanonicalName();

        assertNotNull(canonicalName);
        assertTrue(canonicalName.contains(referenceType.getClass().getName()));
        assertTrue(canonicalName.contains(referenceType.getReferencedType().toCanonical()));
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;

        ReferenceType result = referenceType._narrow(subclass);

        assertNotSame(referenceType, result);
        assertSame(subclass, result.getRawClass());
        assertSame(referenceType.getReferencedType(), result.getReferencedType());
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());

        subclass = Integer.class;

        result = result._narrow(subclass);

        assertNotSame(referenceType, result);
        assertSame(subclass, result.getRawClass());
        assertSame(referenceType.getReferencedType(), result.getReferencedType());
        assertNull(result.getTypeHandler());
        assertNull(result.getValueHandler());

    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();

        assertNotNull(referencedType);
        assertEquals(String.class, referencedType.getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        int count = referenceType.containedTypeCount();

        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        JavaType containedType = referenceType.containedType(0);

        assertNotNull(containedType);
        assertEquals(referenceType.getReferencedType(), containedType);
    }

    @Test
    public void testContainedTypeName() {
        String typeName = referenceType.containedTypeName(0);

        assertNotNull(typeName);
        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> parameterSource = referenceType.getParameterSource();

        assertNotNull(parameterSource);
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder(referenceType.getClass().getName()).append('<')
                .append(referenceType.getReferencedType().toCanonical());

        StringBuilder result = referenceType.getErasedSignature(sb);

        assertSame(sb, result);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder(referenceType.getClass().getName()).append('<')
                .append(referenceType.getReferencedType().getGenericSignature(sb));

        StringBuilder result = referenceType.getGenericSignature(sb);

        assertSame(sb, result);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType equal = ReferenceType.construct(String.class, JavaType.ReferenceType(String.class),
                null, null);
        ReferenceType notEqual1 = ReferenceType.construct(Integer.class, JavaType.ReferenceType(Integer.class),
                null, null);
        ReferenceType notEqual2 = ReferenceType.construct(String.class, JavaType.ReferenceType(Integer.class),
                null, null);

        assertEquals(referenceType, equal);
        assertNotEquals(referenceType, notEqual1);
        assertNotEquals(referenceType, notEqual2);
        assertNotEquals(referenceType, new Object());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class " + referenceType.buildCanonicalName() +
                '<' + referenceType.getReferencedType() + ">]";

        assertEquals(expected, referenceType.toString());
    }
}