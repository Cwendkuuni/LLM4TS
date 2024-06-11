import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = null;
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
    }

    @Test
    public void setReference_ValidInput_ReferenceSet() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void setReference_MultipleTimes_ThrowsIllegalStateException() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        JavaType newRef = new JavaType();
        resolvedRecursiveType.setReference(newRef);
    }

    @Test
    public void getGenericSignature_ReturnsValidSignature() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertNotNull(result);
        assertNotEquals(0, result.length());
    }

    @Test
    public void getErasedSignature_ReturnsValidSignature() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertNotNull(result);
        assertNotEquals(0, result.length());
    }

    @Test
    public void withContentType_ReturnsSameInstance() {
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withTypeHandler_ReturnsSameInstance() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withContentTypeHandler_ReturnsSameInstance() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withValueHandler_ReturnsSameInstance() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withContentValueHandler_ReturnsSameInstance() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withStaticTyping_ReturnsSameInstance() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void _narrow_ReturnsSameInstance() {
        Class<?> subclass = Object.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void refine_ReturnsNull() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }

    @Test
    public void isContainerType_ReturnsFalse() {
        boolean result = resolvedRecursiveType.isContainerType();
        
        assertFalse(result);
    }

    @Test
    public void toString_WithoutReference_ReturnsUnresolved() {
        String expected = "[recursive type; UNRESOLVED]";
        
        String result = resolvedRecursiveType.toString();
        
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void toString_WithReference_ReturnsResolvedClassName() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        
        String result = resolvedRecursiveType.toString();
        
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void equals_SameObject_ReturnsTrue() {
        boolean result = resolvedRecursiveType.equals(resolvedRecursiveType);
        
        assertTrue(result);
    }

    @Test
    public void equals_SameClassDifferentReference_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        
        boolean result = resolvedRecursiveType.equals(other);
        
        assertFalse(result);
    }

    @Test
    public void equals_Null_ReturnsFalse() {
        boolean result = resolvedRecursiveType.equals(null);
        
        assertFalse(result);
    }
}