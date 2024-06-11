
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testWithContentTypeHandler_sameValue() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object h = referenceType.getContentTypeHandler();
        
        assertSame(h, referenceType);
    }
    
    @Test
    public void testWithTypeHandler_sameValue() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object h = referenceType.getTypeHandler();
        
        assertSame(h, referenceType);
    }
    
    @Test
    public void testWithContentValueHandler_sameValue() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object h = referenceType.getContentValueHandler();
        
        assertSame(h, referenceType);
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String canonicalName = referenceType.buildCanonicalName();
        
        assertEquals("java.lang.Object<T>", canonicalName);
    }
    
    @Test
    public void testGetReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType referencedType = referenceType.getReferencedType();
        
        assertNull(referencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        boolean isReference = referenceType.isReferenceType();
        
        assertTrue(isReference);
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType containedType = referenceType.containedType(0);
        
        assertNull(containedType);
    }
    
    @Test
    public void testContainedTypeName() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String containedTypeName = referenceType.containedTypeName(0);
        
        assertNull(containedTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(Object.class, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava.lang.Object;");
        
        assertEquals(expected.toString(), erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava.lang.Object<");
        expected.append(";");

        assertEquals(expected.toString(), genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String str = referenceType.toString();
        
        String expected = "[reference type, class java.lang.Object<T>]";
        
        assertEquals(expected, str);
    }
    
    @Test
    public void testEquals_sameObject() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        boolean equals = referenceType.equals(referenceType);
        
        assertTrue(equals);
    }
    
    @Test
    public void testEquals_differentClass() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType other = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        boolean equals = referenceType.equals(other);
        
        assertFalse(equals);
    }
    
    @Test
    public void testEquals_sameReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType other = new ReferenceType(Object.class, refType, valueHandler, typeHandler, false);
        
        boolean equals = referenceType.equals(other);
        
        assertTrue(equals);
    }
    
    @Test
    public void testEquals_differentReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType other = new ReferenceType(Object.class, JavaType.INT_TYPE, valueHandler, typeHandler, false);
        
        boolean equals = referenceType.equals(other);
        
        assertFalse(equals);
    }

}
