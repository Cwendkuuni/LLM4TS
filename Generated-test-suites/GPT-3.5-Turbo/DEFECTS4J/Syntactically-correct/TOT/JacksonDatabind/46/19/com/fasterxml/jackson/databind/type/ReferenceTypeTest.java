import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    // Test for construct method
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getParameterSource());
        assertEquals(refType, referenceType.getReferencedType());
        assertFalse(referenceType.isReferenceType());
    }

    // Test for withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

        assertEquals(cls, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    // Test for withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object contentTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);

        assertEquals(cls, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    // Test for withValueHandler method
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

        assertEquals(cls, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    // Test for withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object contentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);

        assertEquals(cls, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }

    // Test for withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertEquals(cls, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertTrue(newReferenceType.withStaticTyping().isStaticTyping());
    }

    // Test for buildCanonicalName method
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String canonicalName = cls.getName() + '<' + refType.toCanonical() + '>';
        assertEquals(canonicalName, referenceType.buildCanonicalName());
    }

    // Test for _narrow method
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = (ReferenceType) referenceType._narrow(Integer.class);

        assertEquals(Integer.class, newReferenceType.getParameterSource());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }


    // Test for getReferencedType method
    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(refType, referenceType.getReferencedType());
    }

    // Test for isReferenceType method
    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertTrue(referenceType.isReferenceType());
    }

    // Test for containedTypeCount method
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    // Test for containedType method
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(2));
    }

    // Test for containedTypeName method
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    // Test for getParameterSource method
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(cls, referenceType.getParameterSource());
    }

    // Test for getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        referenceType.getErasedSignature(sb);

        boolean containsSignature = sb.toString().contains(cls.getName());
        assertTrue(containsSignature);
    }

    // Test for getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        referenceType.getGenericSignature(sb);

        boolean containsSignature = sb.toString().contains(refType.getGenericSignature(new StringBuilder()));
        assertTrue(containsSignature);
    }

    // Test for toString method
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String expected = "[reference type, class " + referenceType.buildCanonicalName() + ']';
        assertEquals(expected, referenceType.toString());
    }

    // Test for equals method
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType1 = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
    }

    // Test for serialVersionUID field
    @Test
    public void testSerialVersionUID() {
        assertTrue(ReferenceType.serialVersionUID == 1L);
    }

}