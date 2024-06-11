import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType._class);
        assertEquals(refType.hashCode(), referenceType._hashCode);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
        
        assertEquals(refType, referenceType._referencedType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withTypeHandler(h);
        
        assertSame(referenceType, result);
        assertEquals(h, result._typeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentTypeHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, result._referencedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withValueHandler(h);
        
        assertSame(referenceType, result);
        assertEquals(h, result._valueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentValueHandler(h);
        
        assertSame(referenceType, result);
        assertNotSame(h, result._referencedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        assertFalse(referenceType._asStatic);
        
        ReferenceType result = referenceType.withStaticTyping();
        
        assertSame(referenceType, result);
        assertTrue(result._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        String canonicalName = referenceType.buildCanonicalName();
        
        String expected = cls.getName() + "<" + refType.toCanonical() + ">";
        assertEquals(expected, canonicalName);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        
        JavaType result = referenceType._narrow(subclass);
        
        assertEquals(subclass, result._class);
        assertEquals(refType, result._referencedType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        
        JavaType referencedType = referenceType.getReferencedType();
        
        assertSame(refType, referencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        
        boolean isReferenceType = referenceType.isReferenceType();
        
        assertTrue(isReferenceType);
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();
        
        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        
        JavaType containedType = referenceType.containedType(0);
        
        assertSame(refType, containedType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType();
        
        String containedTypeName = referenceType.containedTypeName(0);
        
        assertEquals("T", containedTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertSame(cls, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        
        StringBuilder result = referenceType.getErasedSignature(sb);
        
        assertSame(sb, result);
        assertEquals(cls.getName(), result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        StringBuilder result = referenceType.getGenericSignature(sb);
        
        assertSame(sb, result);
        assertEquals(cls.getName(), result.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        String str = referenceType.toString();
        
        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        assertEquals(expected, str);
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType1 = new ReferenceType(cls, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType, null, null, false);
        
        boolean result = referenceType1.equals(referenceType2);
        
        assertTrue(result);
    }
}