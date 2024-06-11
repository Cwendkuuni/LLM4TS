import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        // test constructor
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        // test setting a reference
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleReferenceSetting() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        JavaType ref2 = new SimpleType(Double.class);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(null);
    }

    @Test
    public void testInvalidReferenceType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object ref = new Object();
        resolvedRecursiveType.setReference(ref);
        JavaType expected = null;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithNullReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(null);
        StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
        StringBuilder actual = resolvedRecursiveType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToStringWithReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder expected = new StringBuilder("[recursive type; [Ljava.lang.Class;@");
        StringBuilder actual = resolvedRecursiveType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        Class<String> erasedType1 = String.class;
        Class<Integer> erasedType2 = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(new SimpleType(Double.class));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsWithSameClassesDifferentReferences() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new SimpleType(Integer.class);
        resolvedRecursiveType1.setReference(ref1);
        JavaType ref2 = new SimpleType(Double.class);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsWithSameClassesSameReferences() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType(Integer.class);
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsWithNullReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(null);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}