import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.INT_TYPE;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);
        assertNotEquals(referenceType, result);
        assertEquals(h, result._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertNotEquals(referenceType, result);
        assertEquals(h, result._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);
        assertNotEquals(referenceType, result);
        assertEquals(h, result._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertNotEquals(referenceType, result);
        assertEquals(h, result._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertNotEquals(referenceType, result);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertTrue(canonicalName.contains("java.lang.String"));
        assertTrue(canonicalName.contains("int"));
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        Class<?> subclass = Object.class;
        JavaType result = referenceType._narrow(subclass);
        assertNotNull(result);
        assertEquals(subclass, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(JavaType.INT_TYPE, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(JavaType.INT_TYPE, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        String typeName = referenceType.containedTypeName(0);
        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.toString().contains("Ljava/lang/String;"));
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.toString().contains("Ljava/lang/String;"));
        assertTrue(result.toString().contains("int"));
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        String result = referenceType.toString();
        assertNotNull(result);
        assertTrue(result.contains("[reference type, class"));
        assertTrue(result.contains("java.lang.String"));
        assertTrue(result.contains("int"));
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.INT_TYPE, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(new ReferenceType(Integer.class, JavaType.INT_TYPE, null, null, false)));
    }

}