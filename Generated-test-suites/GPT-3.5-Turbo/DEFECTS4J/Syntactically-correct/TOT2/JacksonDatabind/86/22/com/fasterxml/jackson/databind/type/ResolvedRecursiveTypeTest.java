import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType.getSelfReferencedType(), ref);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    // Additional edge cases and exception handling scenarios can be added here

}