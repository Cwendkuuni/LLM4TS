import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {
    private ReferenceType referenceType;

    @Before
    public void setUp() {
        JavaType referencedType = new SimpleType();
        referenceType = new ReferenceType(ReferenceType.class, referencedType, null, null, false);
    }

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(ReferenceType.class, new SimpleType(), null, null);
        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getRawClass());
        assertEquals(SimpleType.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertSame(referenceType, newReferenceType); // Since typeHandler is the same, should return the same instance
        assertNotSame(typeHandler, newReferenceType.getTypeHandler()); // Verify that the type handler was set
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertSame(referenceType, newReferenceType); // Since contentTypeHandler is the same, should return the same instance
        assertNotSame(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler()); // Verify that the content type handler was set
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertSame(referenceType, newReferenceType); // Since valueHandler is the same, should return the same instance
        assertNotSame(valueHandler, newReferenceType.getValueHandler()); // Verify that the value handler was set
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertSame(referenceType, newReferenceType); // Since contentValueHandler is the same, should return the same instance
        assertNotSame(contentValueHandler, newReferenceType.getReferencedType().getValueHandler()); // Verify that the content value handler was set
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertSame(referenceType, newReferenceType); // Since asStatic is the same, should return the same instance
        assertTrue(newReferenceType.isStaticTyping()); // Verify that the static typing was set
    }

    @Test
    public void testBuildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        referenceType.buildCanonicalName(sb);
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>", sb.toString());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = ReferenceType.class;
        JavaType narrowedType = referenceType.narrow(subclass);
        assertTrue(narrowedType instanceof ReferenceType);
        assertSame(subclass, narrowedType.getRawClass()); // Verify that the subclass was set
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
        assertEquals(SimpleType.class, referencedType.getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType containedType = referenceType.containedType(0);
        assertNotNull(containedType);
        assertEquals(SimpleType.class, containedType.getRawClass());
    }

    @Test
    public void testContainedTypeName() {
        assertNull(referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(ReferenceType.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>;", sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType other = new ReferenceType(ReferenceType.class, new SimpleType(), null, null, false);
        assertTrue(referenceType.equals(other));
    }
}

