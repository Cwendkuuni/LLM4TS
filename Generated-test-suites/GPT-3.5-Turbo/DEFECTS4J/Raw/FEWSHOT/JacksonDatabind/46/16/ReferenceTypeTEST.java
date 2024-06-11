import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        JavaType refType = JavaType.refType;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(h);
        
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType.withTypeHandler(h), newReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(referenceType._asStatic, newReferenceType._asStatic);
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType.withStaticTyping(), newReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertTrue(newReferenceType._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        
        assertEquals("java.lang.Integer<java.lang.refType>", canonicalName);
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType(null, referencedType, null, null, false);
        JavaType retrievedReferencedType = referenceType.getReferencedType();
        
        assertSame(referencedType, retrievedReferencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        JavaType referencedType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType(null, referencedType, null, null, false);
        JavaType retrievedReferencedType = referenceType.containedType(0);
        
        assertSame(referencedType, retrievedReferencedType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        String name = referenceType.containedTypeName(0);
        
        assertNull(name);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = Integer.class;
        
        ReferenceType referenceType = new ReferenceType(cls, null, null, null, false);
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(cls, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, false);
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        
        assertSame(sb, erasedSignature);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, false);
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        
        assertSame(sb, genericSignature);
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        String string = referenceType.toString();
        
        assertEquals("[reference type, class java.lang.Integer<java.lang.refType>]", string);
    }
    
    @Test
    public void testEquals_True() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        
        assertTrue(referenceType1.equals(referenceType2));
    }
    
    @Test
    public void testEquals_False_ClassDifferent() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.refType, null, null, false);
        
        assertFalse(referenceType1.equals(referenceType2));
    }
    
    @Test
    public void testEquals_False_ReferencedTypeDifferent() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, JavaType.otherType, null, null, false);
        
        assertFalse(referenceType1.equals(referenceType2));
    }
    
    @Test
    public void testEquals_False_DifferentClass() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        Object obj = new Object();
        
        assertFalse(referenceType1.equals(obj));
    }
    
    @Test
    public void testEquals_False_NullClass() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        
        assertFalse(referenceType1.equals(null));
    }
    
    @Test
    public void testEquals_False_SameClassDifferentReferences() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.refType, null, null, false);
        
        assertFalse(referenceType.equals(referenceType));
    }
}