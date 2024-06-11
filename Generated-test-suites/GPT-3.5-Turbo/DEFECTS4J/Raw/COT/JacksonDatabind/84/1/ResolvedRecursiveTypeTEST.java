import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create a mock JavaType
        JavaType javaType = new JavaTypeMock();

        // Set the reference
        resolvedRecursiveType.setReference(javaType);

        // Assert that the reference is set correctly
        Assert.assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create mock JavaType instances
        JavaType javaType1 = new JavaTypeMock();
        JavaType javaType2 = new JavaTypeMock();

        // Set the reference multiple times
        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create a mock JavaType
        JavaType javaType = new JavaTypeMock();

        // Set the reference
        resolvedRecursiveType.setReference(javaType);

        // Assert that the generic signature is obtained correctly
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        Assert.assertEquals(sb.toString(), resolvedRecursiveType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create a mock JavaType
        JavaType javaType = new JavaTypeMock();

        // Set the reference
        resolvedRecursiveType.setReference(javaType);

        // Assert that the erased signature is obtained correctly
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        Assert.assertEquals(sb.toString(), resolvedRecursiveType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithContentType() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create a mock JavaType
        JavaType contentType = new JavaTypeMock();

        // Assert that the withContentType method returns the same instance
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the withTypeHandler method returns the same instance
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the withContentTypeHandler method returns the same instance
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the withValueHandler method returns the same instance
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the withContentValueHandler method returns the same instance
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the withStaticTyping method returns the same instance
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create a subclass
        Class<?> subclass = Object.class;

        // Assert that the _narrow method returns the same instance
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Create mock parameters
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaTypeMock();
        JavaType[] superInterfaces = new JavaType[]{new JavaTypeMock()};

        // Assert that the refine method returns null
        Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        // Create instance of ResolvedRecursiveType
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the isContainerType method returns false
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringWithUnresolvedReference() {
        // Create instance of ResolvedRecursiveType with unresolved reference
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Assert that the toString() method returns [recursive type; UNRESOLVED]
        Assert.assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringWithResolvedLoop() {
        // Create mock JavaType
        JavaType javaType = new JavaTypeMock();

        // Create instance of ResolvedRecursiveType with resolved loop reference
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(javaType);

        // Assert that the toString() method returns [recursive type; <resolved loop class name>]
        String expected = "[recursive type; " + javaType.getRawClass().getName() + "]";
        Assert.assertEquals(expected, resolvedRecursiveType.toString());
    }
}

class JavaTypeMock extends JavaType {
    public JavaTypeMock() {
        super(null, 0, null, null, null, false, null, null);
    }
}