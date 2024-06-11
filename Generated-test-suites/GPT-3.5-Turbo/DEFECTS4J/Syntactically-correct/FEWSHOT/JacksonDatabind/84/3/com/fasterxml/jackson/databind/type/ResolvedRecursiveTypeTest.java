import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getGenericSignature(sb));
        
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        
        assertEquals(expected, resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getErasedSignature(sb));
        
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        
        assertEquals(expected, resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(Object.class);
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringWhenReferenceIsNull() {
        String expected = "[recursive type; UNRESOLVED]";
        
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringWhenReferenceIsNotNull() {
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        String expected = "[recursive type; java.lang.Object]";
        
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(other));
        
        resolvedRecursiveType.setReference(new JavaType(Object.class));
        other.setReference(new JavaType(Object.class));
        
        assertTrue(resolvedRecursiveType.equals(other));
    }
}