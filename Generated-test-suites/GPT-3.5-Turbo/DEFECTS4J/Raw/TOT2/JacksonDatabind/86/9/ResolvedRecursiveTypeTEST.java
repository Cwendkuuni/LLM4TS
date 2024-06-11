import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new SimpleType(Long.class);
        assertEquals(type, type.withContentType(contentType));
    }

    // Add more test cases for the remaining methods

}