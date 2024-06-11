import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    // Basic test cases
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType referenceType = new SimpleType(Integer.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(referenceType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(referenceType, selfReferencedType);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(String.class);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType3.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType4.setReference(ref2);
        assertFalse(resolvedRecursiveType3.equals(resolvedRecursiveType4));

        ResolvedRecursiveType resolvedRecursiveType5 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType5));
    }

    // Test cases for edge cases and exceptions
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(new SimpleType(Integer.class)); // should throw an IllegalStateException
    }

    @Test
    public void testGetSelfReferencedTypeWhenNull() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceWithNullReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(null); // should throw an IllegalArgumentException
    }

    @Test
    public void testGetSelfReferencedTypeWhenUnresolved() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericSignatureWithNullStringBuilder() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder nullSb = null;
        resolvedRecursiveType.getGenericSignature(nullSb); // should throw a NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testGetErasedSignatureWithNullStringBuilder() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder nullSb = null;
        resolvedRecursiveType.getErasedSignature(nullSb); // should throw a NullPointerException
    }
}