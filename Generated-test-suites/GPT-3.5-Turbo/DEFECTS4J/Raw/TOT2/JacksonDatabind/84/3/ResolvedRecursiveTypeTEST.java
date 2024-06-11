import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, rrt.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, rrt.getErasedSignature(sb));
    }

    // Add more test cases for other methods as needed

}