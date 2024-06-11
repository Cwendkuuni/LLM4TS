import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(JavaType.STRING, referenceType.getReferencedType());
        assertFalse(referenceType.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new TypeHandler();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
        assertSame(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new ContentTypeHandler();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
        assertSame(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new ValueHandler();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
        assertSame(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new ContentValueHandler();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
        assertSame(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
        assertTrue(newReferenceType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        String canonicalName = referenceType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertTrue(canonicalName.startsWith("java.lang.String"));
        assertTrue(canonicalName.contains("java.lang.String"));
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        ReferenceType newReferenceType = (ReferenceType) referenceType._narrow(Object.class);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(Object.class, newReferenceType.getRawClass());
        assertEquals(JavaType.STRING, newReferenceType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
        assertEquals(JavaType.STRING, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        int containedTypeCount = referenceType.containedTypeCount();
        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        JavaType containedType = referenceType.containedType(0);
        assertNotNull(containedType);
        assertEquals(JavaType.STRING, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        String containedTypeName = referenceType.containedTypeName(0);
        assertNotNull(containedTypeName);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertTrue(erasedSignature.toString().startsWith("class java.lang.String"));
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertTrue(genericSignature.toString().startsWith("java.lang.String"));
        assertTrue(genericSignature.toString().contains("java.lang.String"));
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        
        String toString = referenceType.toString();
        assertNotNull(toString);
        assertTrue(toString.startsWith("[reference type, class"));
        assertTrue(toString.contains("java.lang.String"));
        assertTrue(toString.contains("java.lang.String"));
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(Integer.class, JavaType.INT, null, null, false);
        
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals("string"));
    }
}