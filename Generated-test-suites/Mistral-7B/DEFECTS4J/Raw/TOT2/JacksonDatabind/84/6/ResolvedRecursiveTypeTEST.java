import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNotNull(type);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new ArrayListType(); // some JavaType instance
        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
        assertSame(ref, type.getSelfReferencedType());
    }

    @Test(expected = NullPointerException.class)
    public void testSetReferenceWithNullJavaType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = null;
        try {
            type.setReference(ref);
        } catch (IllegalStateException e) {
            // Expected exception, but this is not the expected exception type
            fail("Expected NullPointerException");
        }
    }

    @Test
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref1 = new ArrayListType();
        JavaType ref2 = new HashMapType();
        type.setReference(ref1);
        try {
            type.setReference(ref2);
        } catch (IllegalStateException e) {
            // Expected exception
        }
        assertSame(ref1, type.getSelfReferencedType());
    }

    // Add test cases for other methods as needed

    // Complete test file
}