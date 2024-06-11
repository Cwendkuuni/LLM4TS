import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = new TypeHandler();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, null)
                .withTypeHandler(typeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        Object contentTypeHandler = new TypeHandler();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler)
                .withContentTypeHandler(contentTypeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertEquals(contentTypeHandler, referenceType.getReferencedType().getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new ValueHandler();
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler)
                .withValueHandler(valueHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        Object contentValueHandler = new ValueHandler();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler)
                .withContentValueHandler(contentValueHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertEquals(contentValueHandler, referenceType.getReferencedType().getValueHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler)
                .withStaticTyping();
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertTrue(referenceType.isStaticTyping());
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        String canonicalName = cls.getName() + "<" + refType.toCanonical() + ">";
        
        assertEquals(canonicalName, referenceType.buildCanonicalName());
    }
    
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        Class<?> subclass = Object.class;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler)._narrow(subclass);
        
        assertEquals(subclass, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }
    
    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(refType, referenceType.getReferencedType());
    }
    
    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(refType, referenceType.containedType(0));
    }
    
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals("T", referenceType.containedTypeName(0));
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getParameterSource());
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        
        StringBuilder expected = new StringBuilder();
        expected.append(cls.getName()).append("<").append(refType.getErasedSignature(new StringBuilder())).append(">");
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        
        StringBuilder expected = new StringBuilder();
        expected.append(cls.getName()).append("<").append(refType.getGenericSignature(new StringBuilder())).append(";");
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        String expected = new StringBuilder(40)
            .append("[reference type, class ")
            .append(referenceType.buildCanonicalName())
            .append('<')
            .append(refType)
            .append('>')
            .append(']')
            .toString();
        
        assertEquals(expected, referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertTrue(referenceType1.equals(referenceType2));
    }
}