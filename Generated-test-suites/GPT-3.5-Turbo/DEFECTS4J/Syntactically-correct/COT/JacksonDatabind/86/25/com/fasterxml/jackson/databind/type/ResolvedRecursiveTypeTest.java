
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        resolvedRecursiveType.setReference(new JavaType());

        // Assert
        assertEquals(new JavaType(), resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        resolvedRecursiveType.setReference(new JavaType());
        resolvedRecursiveType.setReference(new JavaType());
    }

    @Test
    public void testGetGenericSignature() {
        // Setup
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());

        // Action
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        // Assert
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        // Setup
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());

        // Action
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        // Assert
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withContentType(new JavaType());

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withTypeHandler(new Object());

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withContentTypeHandler(new Object());

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withValueHandler(new Object());

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withContentValueHandler(new Object());

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.withStaticTyping();

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType._narrow(Class.class);

        // Assert
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        JavaType result = resolvedRecursiveType.refine(Class.class, new TypeBindings(), new JavaType(), new JavaType[] {});

        // Assert
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        boolean result = resolvedRecursiveType.isContainerType();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testToStringUnresolved() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());

        // Action
        String result = resolvedRecursiveType.toString();

        // Assert
        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testToStringResolved() {
        // Setup
        Class<?> resolvedClass = TestClass.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType(resolvedClass));

        // Action
        String result = resolvedRecursiveType.toString();

        // Assert
        assertEquals("[recursive type; " + resolvedClass.getName() + "]", result);
    }

    @Test
    public void testEqualsSelf() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());

        // Action
        boolean result = resolvedRecursiveType.equals(resolvedRecursiveType);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEqualsNull() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());

        // Action
        boolean result = resolvedRecursiveType.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentClass() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());

        // Action
        boolean result = resolvedRecursiveType.equals(new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEqualsEqualReferences() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType1.setReference(new JavaType());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType2.setReference(new JavaType());

        // Action
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEqualsDifferentReferences() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType1.setReference(new JavaType(Class.class));
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType2.setReference(new JavaType(String.class));

        // Action
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        // Assert
        assertFalse(result);
    }
}
