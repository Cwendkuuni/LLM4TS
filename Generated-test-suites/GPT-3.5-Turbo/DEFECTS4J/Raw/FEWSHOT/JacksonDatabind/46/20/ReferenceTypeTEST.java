import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.fromClass(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        
        assertEquals(h, result.getReferencedType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);
        
        assertEquals(h, result.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        
        assertEquals(h, result.getReferencedType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        
        assertTrue(result.getAsStatic());
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        
        assertEquals("java.lang.String<Integer>", canonicalName);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.fromClass(Integer.class), null, null, false);
        ReferenceType result = (ReferenceType)referenceType.narrow(subclass);
        
        assertEquals(subclass, result.getClass());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }
}