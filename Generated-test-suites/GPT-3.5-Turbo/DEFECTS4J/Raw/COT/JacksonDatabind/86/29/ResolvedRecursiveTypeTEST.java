
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        type.setReference(ref);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), type.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), type.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        assertEquals(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(type, type.withTypeHandler(h));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(type, type.withContentTypeHandler(h));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(type, type.withValueHandler(h));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(type, type.withContentValueHandler(h));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        assertEquals(type, type._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder(40);
        sb.append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), type.toString());
        
        type.setReference(new JavaType());
        sb = new StringBuilder(40);
        sb.append("[recursive type; java.lang.Object");
        assertEquals(sb.toString(), type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        
        type1.setReference(new JavaType());
        type2.setReference(new JavaType());
        assertFalse(type1.equals(type2));
        
        JavaType ref = new JavaType();
        type2.setReference(ref);
        assertTrue(type1.equals(type2));
        
    }
}
