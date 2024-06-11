import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getJavaType());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertEquals(asStatic, referenceType.isStatic());
    }
    
    @Test
    public void testConstructorWithBase() {
        TypeBase base = null;
        JavaType refType = null;
        
        ReferenceType referenceType = new ReferenceType(base, refType);
        
        assertNotNull(referenceType);
        assertEquals(base.getJavaType(), referenceType.getJavaType());
        assertEquals(base.getTypeBindings(), referenceType.getTypeBindings());
        assertEquals(base.getSuperClass(), referenceType.getSuperClass());
        assertArrayEquals(base.getSuperInterfaces(), referenceType.getSuperInterfaces());
        assertEquals(base.getValueHandler(), referenceType.getValueHandler());
        assertEquals(base.getTypeHandler(), referenceType.getTypeHandler());
        assertEquals(base.isStatic(), referenceType.isStatic());
        assertEquals(base.getReferencedType(), referenceType.getReferencedType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType refdType = null;
        
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        
        assertNotNull(referenceType);
        assertEquals(refdType, referenceType.getReferencedType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testConstruct() {
        Class<?> cls = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getJavaType());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testConstructWithClass() {
        Class<?> cls = null;
        JavaType refType = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getJavaType());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withContentType(contentType);
        
        assertNotNull(referenceType);
        assertEquals(contentType, referenceType.getReferencedType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withTypeHandler(h);
        
        assertNotNull(referenceType);
        assertEquals(h, referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withContentTypeHandler(h);
        
        assertNotNull(referenceType);
        assertEquals(h, referenceType.getReferencedType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withValueHandler(h);
        
        assertNotNull(referenceType);
        assertEquals(h, referenceType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withContentValueHandler(h);
        
        assertNotNull(referenceType);
        assertEquals(h, referenceType.getReferencedType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType()
                .withStaticTyping();
        
        assertNotNull(referenceType);
        assertTrue(referenceType.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        ReferenceType referenceType = new ReferenceType()
                .refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(referenceType);
        assertEquals(rawType, referenceType.getJavaType());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertArrayEquals(superInterfaces, referenceType.getSuperInterfaces());
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType();
        String expectedName = referenceType.getJavaType().getName() + "<" + referenceType.getReferencedType().toCanonical() + ">";
        
        assertEquals(expectedName, referenceType.buildCanonicalName());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        
        ReferenceType referenceType = new ReferenceType()
                ._narrow(subclass);
        
        assertNotNull(referenceType);
        assertEquals(subclass, referenceType.getJavaType());
    }
    
    @Test
    public void testGetContentType() {
        JavaType refType = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withContentType(refType);
        
        assertEquals(refType, referenceType.getContentType());
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType refType = null;
        
        ReferenceType referenceType = new ReferenceType()
                .withContentType(refType);
        
        assertEquals(refType, referenceType.getReferencedType());
    }
    
    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType();
        
        assertTrue(referenceType.hasContentType());
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        String expectedSignature = referenceType.getJavaType().getName();
        
        referenceType.getErasedSignature(sb);
        
        assertEquals(expectedSignature, sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        String expectedSignature = referenceType.buildCanonicalName();
        
        referenceType.getGenericSignature(sb);
        
        assertEquals(expectedSignature, sb.toString());
    }
    
    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        
        assertTrue(referenceType.isAnchorType());
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        String expectedString = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        
        assertEquals(expectedString, referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = new ReferenceType();
        
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType2));
    }

}