import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(null, referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Object h = new Object();
        ReferenceType result = referenceType.withTypeHandler(h);
        
        assertEquals(referenceType, result);
        assertEquals(h, result.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Object h = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(h);
        
        assertEquals(referenceType, result);
        assertEquals(h, result.getReferencedType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Object h = new Object();
        ReferenceType result = referenceType.withValueHandler(h);
        
        assertEquals(referenceType, result);
        assertEquals(h, result.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Object h = new Object();
        ReferenceType result = referenceType.withContentValueHandler(h);
        
        assertEquals(referenceType, result);
        assertEquals(h, result.getReferencedType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        ReferenceType result = referenceType.withStaticTyping();
        
        assertEquals(referenceType, result);
        assertTrue(result.isStaticTyping());
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        String expectedName = cls.getName() + "<" + refType.toCanonical() + ">";
        
        assertEquals(expectedName, referenceType.buildCanonicalName());
    }
    
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Class<?> subclass = Object.class;
        ReferenceType result = (ReferenceType) referenceType._narrow(subclass);
        
        assertEquals(subclass, result.getRawClass());
        assertEquals(refType, result.getReferencedType());
    }
    
    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(refType, referenceType.getReferencedType());
    }
    
    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(refType, referenceType.containedType(0));
        assertEquals(null, referenceType.containedType(1));
    }
    
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals("T", referenceType.containedTypeName(0));
        assertEquals(null, referenceType.containedTypeName(1));
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        assertEquals(cls, referenceType.getParameterSource());
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());
        
        assertEquals(cls.getName(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());
        
        String expectedSignature = cls.getName() + "<" + refType.getGenericSignature(new StringBuilder()) + ";";
        assertEquals(expectedSignature, sb.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        String expectedString = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        
        assertEquals(expectedString, referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, null, null);
        
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType2));
    }
}