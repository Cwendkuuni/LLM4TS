import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    // Test constructor
    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    // Test withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        Object contentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentValueHandler);

        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    // Test withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);

        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertTrue(newReferenceType.isStatic());
    }

    // Test buildCanonicalName method
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);

        String expectedCanonicalName = "java.lang.String<java.lang.Integer>";
        assertEquals(expectedCanonicalName, referenceType.buildCanonicalName());
    }

    // Test _narrow method
    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, false);
        ReferenceType newReferenceType = referenceType._narrow(Integer.class);

        assertEquals(Integer.class, newReferenceType.getClass());
        assertFalse(newReferenceType.isStatic());
    }

    // Test getReferencedType method
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertEquals(JavaType.INT_TYPE, referenceType.getReferencedType());
    }

    // Test isReferenceType method
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    // Test containedTypeCount method
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    // Test containedType method
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertEquals(JavaType.INT_TYPE, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    // Test containedTypeName method
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    // Test getParameterSource method
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        assertEquals(Class.class, referenceType.getParameterSource());
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(Class.class, JavaType.INT_TYPE, null, null, false);

        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("Ljava/lang/Class<");
        expectedSignature.append("T>;");
        assertEquals(expectedSignature.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);

        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("Ljava/lang/String;");
        expectedSignature.append("<");
        expectedSignature.append("Ljava/lang/Integer;");
        expectedSignature.append(">;");
        assertEquals(expectedSignature.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    // Test toString method
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);

        String expectedToString = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedToString, referenceType.toString());
    }

    // Test equals method
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(Integer.class, JavaType.INT_TYPE, null, null, false);

        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }
}