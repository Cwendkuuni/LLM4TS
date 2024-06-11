
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType result = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertSame(cls, result.getInternalClass());
        assertSame(refType, result.getReferencedType());
        assertSame(valueHandler, result.getValueHandler());
        assertSame(typeHandler, result.getTypeHandler());
        assertFalse(result.isAsStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        
        ReferenceType result = referenceType.withTypeHandler(typeHandler);
        
        assertSame(referenceType.getInternalClass(), result.getInternalClass());
        assertSame(referenceType.getReferencedType(), result.getReferencedType());
        assertSame(referenceType.getValueHandler(), result.getValueHandler());
        assertSame(typeHandler, result.getTypeHandler());
        assertEquals(referenceType.isAsStatic(), result.isAsStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        
        ReferenceType result = referenceType.withContentTypeHandler(contentTypeHandler);
        
        assertSame(referenceType.getInternalClass(), result.getInternalClass());
        assertSame(referenceType.getReferencedType().getTypeHandler(), contentTypeHandler);
        assertSame(referenceType.getValueHandler(), result.getValueHandler());
        assertSame(referenceType.getTypeHandler(), result.getTypeHandler());
        assertEquals(referenceType.isAsStatic(), result.isAsStatic());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        
        ReferenceType result = referenceType.withValueHandler(valueHandler);
        
        assertSame(referenceType.getInternalClass(), result.getInternalClass());
        assertSame(referenceType.getReferencedType(), result.getReferencedType());
        assertSame(valueHandler, result.getValueHandler());
        assertSame(referenceType.getTypeHandler(), result.getTypeHandler());
        assertEquals(referenceType.isAsStatic(), result.isAsStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        
        ReferenceType result = referenceType.withContentValueHandler(contentValueHandler);
        
        assertSame(referenceType.getInternalClass(), result.getInternalClass());
        assertSame(referenceType.getReferencedType().getValueHandler(), contentValueHandler);
        assertSame(referenceType.getValueHandler(), result.getValueHandler());
        assertSame(referenceType.getTypeHandler(), result.getTypeHandler());
        assertEquals(referenceType.isAsStatic(), result.isAsStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();
        
        assertSame(referenceType.getInternalClass(), result.getInternalClass());
        assertSame(referenceType.getReferencedType().withStaticTyping(), result.getReferencedType());
        assertSame(referenceType.getValueHandler(), result.getValueHandler());
        assertSame(referenceType.getTypeHandler(), result.getTypeHandler());
        assertTrue(result.isAsStatic());
    }
    
    @Test
    public void testBuildCanonicalName() {
        String expected = "com.fasterxml.jackson.databind.type.ReferenceType<T>";
        
        String result = referenceType.buildCanonicalName();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new JavaType();
        
        JavaType result = referenceType.getReferencedType();
        
        assertSame(referencedType, result);
    }
    
    @Test
    public void testIsReferenceType() {
        boolean result = referenceType.isReferenceType();
        
        assertTrue(result);
    }
    
    @Test
    public void testContainedTypeCount() {
        int result = referenceType.containedTypeCount();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testContainedType() {
        JavaType referencedType = new JavaType();
        
        JavaType result = referenceType.containedType(0);
        
        assertSame(referencedType, result);
    }
    
    @Test
    public void testContainedTypeName() {
        String result = referenceType.containedTypeName(0);
        
        assertEquals("T", result);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> result = referenceType.getParameterSource();
        
        assertSame(referenceType.getInternalClass(), result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = referenceType.getErasedSignature(sb);
        
        assertEquals(referenceType.getInternalClassSignature(referenceType.getInternalClass(), sb, true), result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = referenceType.getGenericSignature(sb);
        
        StringBuilder expected = new StringBuilder();
        expected.append(referenceType.getInternalClassSignature(referenceType.getInternalClass(), sb, false));
        expected.append('<');
        expected.append(referenceType.getReferencedType().getGenericSignature(sb));
        expected.append(';');
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testToString() {
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<T><com.fasterxml.jackson.databind.JavaType>]";
        
        String result = referenceType.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals() {
        ReferenceType other = new ReferenceType();
        other.setInternalClass(referenceType.getInternalClass());
        other.setReferencedType(referenceType.getReferencedType());
        
        boolean result = referenceType.equals(other);
        
        assertTrue(result);
    }
}
