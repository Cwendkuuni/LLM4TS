import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Void.class, new TypeBindings());
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());

        assertTrue(type1.equals(type1));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(type2));

        type1.setReference(new ResolvedRecursiveType(Double.class, new TypeBindings()));
        type2.setReference(new ResolvedRecursiveType(Integer.class, new TypeBindings()));
        assertFalse(type1.equals(type2));
    }

    // Add more test cases for other methods as needed

}