import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        rrt.setReference(ref);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        rrt.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        rrt.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType();
        assertEquals(rrt, rrt.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        assertEquals(rrt, rrt.withTypeHandler(h));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        assertEquals(rrt, rrt.withContentTypeHandler(h));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        assertEquals(rrt, rrt.withValueHandler(h));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Object h = new Object();
        assertEquals(rrt, rrt.withContentValueHandler(h));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertEquals(rrt, rrt.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertEquals(rrt, rrt._narrow(String.class));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(rrt.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertFalse(rrt.isContainerType());
    }
    
    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, rrt.toString());
    }
    
    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        String expected = "[recursive type; java.lang.String]";
        assertEquals(expected, rrt.toString());
    }
    
    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertTrue(rrt.equals(rrt));
    }
    
    @Test
    public void testEqualsNull() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertFalse(rrt.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertFalse(rrt.equals("string"));
    }
    
    @Test
    public void testEqualsUnresolved() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, null);
        assertFalse(rrt1.equals(rrt2));
    }
    
    @Test
    public void testEqualsResolved() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        rrt1.setReference(ref);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, null);
        rrt2.setReference(ref);
        assertTrue(rrt1.equals(rrt2));
    }
}