import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        rrt.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), rrt.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        rrt.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), rrt.getErasedSignature(sb));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        assertEquals(rrt, rrt.withValueHandler(handler));
    }

    @Test
    public void testToSting() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED", rrt.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        rrt1.setReference(ref);
        rrt2.setReference(ref);
        assertTrue(rrt1.equals(rrt2));
    }
}