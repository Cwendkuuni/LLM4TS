import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Test setting reference
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(SomeClass.class, bindings);
        JavaType ref = new JavaType(SomeClass.class);
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        // Test setting reference multiple times
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(SomeClass.class, bindings);
        JavaType ref1 = new JavaType(SomeClass.class);
        JavaType ref2 = new JavaType(AnotherClass.class);
        rrt.setReference(ref1);
        rrt.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        // Test getting generic signature
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(SomeClass.class, bindings);
        JavaType ref = new JavaType(SomeClass.class);
        rrt.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), rrt.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        // Test getting erased signature
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(SomeClass.class, bindings);
        JavaType ref = new JavaType(SomeClass.class);
        rrt.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), rrt.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        // Test with content type
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(SomeClass.class, bindings);
        assertEquals(rrt, rrt.withContentType(null));
    }

    // Add more test cases for the remaining methods

}