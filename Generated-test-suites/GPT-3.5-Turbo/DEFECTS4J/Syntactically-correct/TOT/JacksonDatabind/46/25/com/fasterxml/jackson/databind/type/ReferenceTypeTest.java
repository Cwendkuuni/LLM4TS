import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(cls, rt._class);
        assertEquals(refType, rt._referencedType);
        assertNull(rt._valueHandler);
        assertNull(rt._typeHandler);
        assertFalse(rt._asStatic);
    }
    
    @Test
    public void testWithTypeHandler_SameHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object typeHandler = new TypeHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, typeHandler, false);
        ReferenceType result = rt.withTypeHandler(typeHandler);
        
        assertSame(rt, result);
    }
    
    @Test
    public void testWithTypeHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object typeHandler1 = new TypeHandler();
        Object typeHandler2 = new TypeHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, typeHandler1, false);
        ReferenceType result = rt.withTypeHandler(typeHandler2);
        
        assertNotSame(rt, result);
        assertEquals(typeHandler2, result._typeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler_SameHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object typeHandler = new TypeHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        ReferenceType result = rt.withContentTypeHandler(typeHandler);
        
        assertSame(rt, result);
    }
    
    @Test
    public void testWithContentTypeHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object typeHandler1 = new TypeHandler();
        Object typeHandler2 = new TypeHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        ReferenceType result = rt.withContentTypeHandler(typeHandler2);
        
        assertNotSame(rt, result);
        assertEquals(typeHandler2, result._referencedType.<Object>getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler_SameHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new ValueHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, null, false);
        ReferenceType result = rt.withValueHandler(valueHandler);
        
        assertSame(rt, result);
    }
    
    @Test
    public void testWithValueHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler1 = new ValueHandler();
        Object valueHandler2 = new ValueHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, valueHandler1, null, false);
        ReferenceType result = rt.withValueHandler(valueHandler2);
        
        assertNotSame(rt, result);
        assertEquals(valueHandler2, result._valueHandler);
    }
    
    @Test
    public void testWithContentValueHandler_SameHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new ValueHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        ReferenceType result = rt.withContentValueHandler(valueHandler);
        
        assertSame(rt, result);
    }
    
    @Test
    public void testWithContentValueHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler1 = new ValueHandler();
        Object valueHandler2 = new ValueHandler();
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        ReferenceType result = rt.withContentValueHandler(valueHandler2);
        
        assertNotSame(rt, result);
        assertEquals(valueHandler2, result._referencedType.<Object>getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        ReferenceType result = rt.withStaticTyping();
        
        assertNotSame(rt, result);
        assertTrue(result._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        String canonicalName = rt.buildCanonicalName();
        
        assertEquals("java.lang.Object<java.lang.Integer>", canonicalName);
    }
    
    // Additional test cases
    
    @Test
    public void testNarrow() {
        Class<?> cls = Number.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        JavaType result = rt._narrow(Integer.class);
        
        assertEquals(Integer.class, result._class);
        assertEquals(refType, result._referencedType);
    }
    
    @Test
    public void testGetReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        JavaType result = rt.getReferencedType();
        
        assertSame(refType, result);
    }
    
    @Test
    public void testIsReferenceType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        boolean isReferenceType = rt.isReferenceType();
        
        assertTrue(isReferenceType);
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        int count = rt.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType_ExistingIndex() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        JavaType result = rt.containedType(0);
        
        assertSame(refType, result);
    }
    
    @Test
    public void testContainedType_InvalidIndex() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        JavaType result = rt.containedType(1);
        
        assertNull(result);
    }
    
    @Test
    public void testContainedTypeName_ExistingIndex() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        String result = rt.containedTypeName(0);
        
        assertEquals("T", result);
    }
    
    @Test
    public void testContainedTypeName_InvalidIndex() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        String result = rt.containedTypeName(1);
        
        assertNull(result);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        Class<?> parameterSource = rt.getParameterSource();
        
        assertEquals(cls, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        StringBuilder sb = rt.getErasedSignature(new StringBuilder());
        
        assertEquals("L" + cls.getName() + ";", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        StringBuilder sb = rt.getGenericSignature(new StringBuilder());
        
        assertEquals("L" + cls.getName() + "<" + refType.getGenericSignature(new StringBuilder()) + ";", 
                     sb.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        String toString = rt.toString();
        
        assertEquals("[reference type, class " + rt.buildCanonicalName() + "]", toString);
    }
    
    @Test
    public void testEquals_SameObject() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        
        assertTrue(rt.equals(rt));
    }
    
    @Test
    public void testEquals_NullObject() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        
        assertFalse(rt.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt = new ReferenceType(cls, refType, null, null, false);
        
        assertFalse(rt.equals(new SimpleType(String.class)));
    }
    
    @Test
    public void testEquals_DifferentClassType() {
        Class<?> cls1 = Object.class;
        Class<?> cls2 = String.class;
        JavaType refType = new SimpleType(Integer.class);
        
        ReferenceType rt1 = new ReferenceType(cls1, refType, null, null, false);
        ReferenceType rt2 = new ReferenceType(cls2, refType, null, null, false);
        
        assertFalse(rt1.equals(rt2));
    }
    
    @Test
    public void testEquals_DifferentReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(String.class);
        
        ReferenceType rt1 = new ReferenceType(cls, refType1, null, null, false);
        ReferenceType rt2 = new ReferenceType(cls, refType2, null, null, false);
        
        assertFalse(rt1.equals(rt2));
    }
    
    @Test
    public void testEquals_EqualObjects() {
        Class<?> cls = Object.class;
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        
        ReferenceType rt1 = new ReferenceType(cls, refType1, null, null, false);
        ReferenceType rt2 = new ReferenceType(cls, refType2, null, null, false);
        
        assertTrue(rt1.equals(rt2));
    }
    
    // Other test cases for edge cases and error scenarios can be added here
    
}