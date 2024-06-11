import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        recursiveType.setReference(ref);

        assertEquals(ref, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_multipleTimes() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        
        recursiveType.setReference(ref1);
        recursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        recursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        recursiveType.getGenericSignature(sb);
        String expectedSignature = ref.getGenericSignature(new StringBuilder()).toString();
        
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        recursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        recursiveType.getErasedSignature(sb);
        String expectedSignature = ref.getErasedSignature(new StringBuilder()).toString();
        
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType(String.class);
        
        JavaType result = recursiveType.withContentType(contentType);
        
        assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = recursiveType.withTypeHandler(handler);
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = recursiveType.withContentTypeHandler(handler);
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = recursiveType.withValueHandler(handler);
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = recursiveType.withContentValueHandler(handler);
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = recursiveType.withStaticTyping();
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = String.class;
        
        JavaType result = recursiveType._narrow(subclass);
        
        assertEquals(recursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = {};
        
        JavaType result = recursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString_unresolved() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        String expectedToString = "[recursive type; UNRESOLVED]";
        
        assertEquals(expectedToString, recursiveType.toString());
    }

    @Test
    public void testToString_resolved() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(Integer.class);
        recursiveType.setReference(ref);
        
        String expectedToString = "[recursive type; " + ref.getRawClass().getName() + "]";
        
        assertEquals(expectedToString, recursiveType.toString());
    }

    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertTrue(recursiveType.equals(recursiveType));
    }

    @Test
    public void testEquals_sameTypeAndReference() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        
        recursiveType1.setReference(ref);
        recursiveType2.setReference(ref);
        
        assertTrue(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_differentObjectClasses() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertFalse(recursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_differentReference() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        
        recursiveType1.setReference(ref1);
        recursiveType2.setReference(ref2);
        
        assertFalse(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        JavaType ref = new JavaType(String.class);
        
        recursiveType1.setReference(ref);
        
        assertFalse(recursiveType1.equals(recursiveType2));
    }

}