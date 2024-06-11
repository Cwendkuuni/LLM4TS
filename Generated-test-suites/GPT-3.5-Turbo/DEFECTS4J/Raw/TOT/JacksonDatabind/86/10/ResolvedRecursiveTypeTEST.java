import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType();
        rrType.setReference(ref);
        assertEquals(ref, rrType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        rrType.setReference(ref1);
        rrType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        rrType.setReference(new JavaType());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        rrType.getSelfReferencedType().getGenericSignature(expected);
        assertEquals(expected.toString(), rrType.getGenericSignature(sb).toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        rrType.setReference(new JavaType());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        rrType.getSelfReferencedType().getErasedSignature(expected);
        assertEquals(expected.toString(), rrType.getErasedSignature(sb).toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType();
        assertEquals(rrType, rrType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        assertEquals(rrType, rrType.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        assertEquals(rrType, rrType.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        assertEquals(rrType, rrType.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        assertEquals(rrType, rrType.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(rrType, rrType.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = Object.class;
        assertEquals(rrType, rrType._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(rrType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(rrType.isContainerType());
    }
    
    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", rrType.toString());
    }
    
    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        rrType.setReference(new JavaType());
        assertEquals("[recursive type; " + rrType.getSelfReferencedType().getRawClass().getName() + "]", rrType.toString());
    }
    
    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        assertTrue(rrType.equals(rrType));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType rrType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(rrType.equals("string"));
    }
    
    @Test
    public void testEqualsDifferentReferenceType() {
        ResolvedRecursiveType rrType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType rrType2 = new ResolvedRecursiveType(Object.class, null);
        rrType1.setReference(new JavaType());
        rrType2.setReference(new JavaType());
        assertFalse(rrType1.equals(rrType2));
    }
    
    @Test
    public void testEqualsSameReferenceType() {
        ResolvedRecursiveType rrType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType rrType2 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType();
        rrType1.setReference(ref);
        rrType2.setReference(ref);
        assertTrue(rrType1.equals(rrType2));
    }
}