import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {
    
    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(Integer.class, JavaType.INT, null, null, false);
    }

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(Float.class, JavaType.FLOAT, null, null);
        assertNotNull(referenceType);
        assertEquals(Float.class, referenceType.getRawClass());
        assertEquals(JavaType.FLOAT, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
    }

    @Test
    public void testWithTypeHandler_SameHandler() {
        ReferenceType modifiedType = referenceType.withTypeHandler(referenceType.getTypeHandler());
        assertSame(referenceType, modifiedType);
    }

    @Test
    public void testWithTypeHandler_DifferentHandler() {
        Object newHandler = new Object();
        ReferenceType modifiedType = referenceType.withTypeHandler(newHandler);
        assertNotSame(referenceType, modifiedType);
        assertEquals(newHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler_SameHandler() {
        ReferenceType modifiedType = referenceType.withContentTypeHandler(referenceType.getReferencedType().getTypeHandler());
        assertSame(referenceType, modifiedType);
    }

    @Test
    public void testWithContentTypeHandler_DifferentHandler() {
        Object newHandler = new Object();
        ReferenceType modifiedType = referenceType.withContentTypeHandler(newHandler);
        assertNotSame(referenceType, modifiedType);
        assertEquals(newHandler, modifiedType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler_SameHandler() {
        ReferenceType modifiedType = referenceType.withValueHandler(referenceType.getValueHandler());
        assertSame(referenceType, modifiedType);
    }

    @Test
    public void testWithValueHandler_DifferentHandler() {
        Object newHandler = new Object();
        ReferenceType modifiedType = referenceType.withValueHandler(newHandler);
        assertNotSame(referenceType, modifiedType);
        assertEquals(newHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler_SameHandler() {
        ReferenceType modifiedType = referenceType.withContentValueHandler(referenceType.getReferencedType().getValueHandler());
        assertSame(referenceType, modifiedType);
    }

    @Test
    public void testWithContentValueHandler_DifferentHandler() {
        Object newHandler = new Object();
        ReferenceType modifiedType = referenceType.withContentValueHandler(newHandler);
        assertNotSame(referenceType, modifiedType);
        assertEquals(newHandler, modifiedType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping_SameTyping() {
        ReferenceType modifiedType = referenceType.withStaticTyping();
        assertSame(referenceType, modifiedType);
    }

    @Test
    public void testWithStaticTyping_DifferentTyping() {
        ReferenceType modifiedType = referenceType.withStaticTyping();
        assertTrue(modifiedType.isJavaLangObject());
        assertTrue(modifiedType.isReferenceType());
        assertTrue(modifiedType.getReferencedType().isJavaLangObject());
        assertTrue(modifiedType.getReferencedType().isReferenceType());
        assertEquals(Object.class, modifiedType.getRawClass());
        assertEquals(referenceType.getValueHandler(), modifiedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), modifiedType.getTypeHandler());
    }

    @Test
    public void testBuildCanonicalName() {
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("java.lang.Integer<int>", canonicalName);
    }

    @Test
    public void testNarrow() {
        ReferenceType modifiedType = referenceType._narrow(Number.class);
        assertNotSame(referenceType, modifiedType);
        assertEquals(Number.class, modifiedType.getRawClass());
        assertEquals(Integer.class, modifiedType.getReferencedType().getRawClass());
        assertEquals(referenceType.getValueHandler(), modifiedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), modifiedType.getTypeHandler());
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(JavaType.INT, referencedType);
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
        assertEquals(JavaType.INT, containedType);
    }

    @Test
    public void testContainedTypeName() {
        String typeName = referenceType.containedTypeName(0);
        assertNull(typeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(Integer.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Integer;", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Integer<[I>;", result.toString());
    }

    @Test
    public void testToString() {
        String toString = referenceType.toString();
        assertEquals("[reference type, class java.lang.Integer<int>]", toString);
    }

    @Test
    public void testEquals_SameObject() {
        assertEquals(referenceType, referenceType);
    }

    @Test
    public void testEquals_SameAttributes() {
        ReferenceType anotherType = new ReferenceType(Integer.class, JavaType.INT, null, null, false);
        assertEquals(referenceType, anotherType);
    }

    @Test
    public void testEquals_DifferentClass() {
        ReferenceType anotherType = new ReferenceType(Float.class, JavaType.INT, null, null, false);
        assertNotEquals(referenceType, anotherType);
    }

    @Test
    public void testEquals_DifferentReferencedType() {
        ReferenceType anotherType = new ReferenceType(Integer.class, JavaType.FLOAT, null, null, false);
        assertNotEquals(referenceType, anotherType);
    }
}