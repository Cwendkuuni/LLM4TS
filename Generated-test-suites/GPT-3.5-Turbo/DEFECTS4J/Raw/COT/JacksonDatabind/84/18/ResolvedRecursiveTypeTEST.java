import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(SomeClass.class, new TypeBindings());
        JavaType reference = new JavaType(SomeClass.class);
        type.setReference(reference);

        assertEquals(reference, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleCalls() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(SomeClass.class, new TypeBindings());
        JavaType reference1 = new JavaType(SomeClass.class);
        JavaType reference2 = new JavaType(AnotherClass.class);

        type.setReference(reference1);

        // This should throw an IllegalStateException
        type.setReference(reference2);
    }
    
    // Add test cases for other methods here

}