import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, referenceType._typeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, referenceType._referencedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, referenceType._valueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, referenceType._referencedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        
        assertSame(referenceType, result);
        assertTrue(referenceType._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("java.lang.Object<T>", canonicalName);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        JavaType result = referenceType._narrow(subclass);
        
        assertTrue(result instanceof ReferenceType);
        assertSame(subclass, result._class);
        assertEquals(referenceType._referencedType, ((ReferenceType) result)._referencedType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        
        assertEquals(referenceType._referencedType, referencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        JavaType containedType = referenceType.containedType(0);
        
        assertEquals(referenceType._referencedType, containedType);
        
        JavaType nonExistentType = referenceType.containedType(1);
        assertNull(nonExistentType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        String typeName = referenceType.containedTypeName(0);
        
        assertEquals("T", typeName);
        
        String nonExistentTypeName = referenceType.containedTypeName(1);
        assertNull(nonExistentTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(referenceType._class, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        
        assertSame(sb, erasedSignature);
        assertEquals("Ljava.lang.Object;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        
        assertSame(sb, genericSignature);
        assertEquals("Ljava.lang.Object<T>;", genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        String string = referenceType.toString();
        
        assertEquals("[reference type, class java.lang.Object<java.lang.Object>]", string);
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType equalType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType differentClassType = new ReferenceType(String.class, new SimpleType(Object.class), null, null, false);
        ReferenceType differentReferencedType = new ReferenceType(Object.class, new SimpleType(String.class), null, null, false);
        
        assertTrue(referenceType.equals(equalType));
        assertFalse(referenceType.equals(differentClassType));
        assertFalse(referenceType.equals(differentReferencedType));
    }
}