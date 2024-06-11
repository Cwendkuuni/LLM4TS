import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        JavaType referencedType = new SimpleType(Object.class);
        referenceType = new ReferenceType(String.class, referencedType, null, null, false);
    }

    @Test
    public void testConstruct() {
        ReferenceType type = ReferenceType.construct(String.class, referenceType.getReferencedType(), null, null);
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(Object.class, type.getReferencedType().getClass());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType type = referenceType.withTypeHandler(null);
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
        assertNull(type.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType type = referenceType.withContentTypeHandler(null);
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
        assertNull(type.getContentValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType type = referenceType.withValueHandler(null);
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
        assertNull(type.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType type = referenceType.withContentValueHandler(null);
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
        assertNull(type.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType type = referenceType.withStaticTyping();
        assertNotNull(type);
        assertEquals(String.class, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
        assertTrue(type.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("java.lang.String<java.lang.Object>", canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = StringBuilder.class;
        ReferenceType type = referenceType.narrow(subclass);
        assertNotNull(type);
        assertEquals(subclass, type.getClass());
        assertEquals(referenceType.getReferencedType(), type.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
        assertEquals(Object.class, referencedType.getClass());
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
        assertNull(typeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertNotNull(erasedSignature.toString());
        assertTrue(erasedSignature.length() > 0);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertNotNull(genericSignature.toString());
        assertTrue(genericSignature.length() > 0);
    }

    @Test
    public void testToString() {
        String stringRepresentation = referenceType.toString();
        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains("reference type"));
        assertTrue(stringRepresentation.contains("class java.lang.String"));
        assertTrue(stringRepresentation.contains("<java.lang.Object>"));
    }

    @Test
    public void testEquals() {
        ReferenceType sameType = new ReferenceType(String.class, referenceType.getReferencedType(), null, null, false);
        assertTrue(referenceType.equals(sameType));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ReferenceType differentType = new ReferenceType(Object.class, referenceType.getReferencedType(), null, null, false);
        assertFalse(referenceType.equals(differentType));
    }

    @Test
    public void testEqualsWithDifferentReferencedType() {
        JavaType differentReferencedType = new SimpleType(Boolean.class);
        ReferenceType differentType = new ReferenceType(String.class, differentReferencedType, null, null, false);
        assertFalse(referenceType.equals(differentType));
    }

}