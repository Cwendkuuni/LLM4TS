import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.ClassCastException;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Assert;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        JavaType javaType = new JavaType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(String.class, resolvedRecursiveType.getRawClass());
        assertEquals(javaType, resolvedRecursiveType.getBindingFromCurrentType());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(javaType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(javaType, selfReferencedType);
    }

    // Test other methods with various input values

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceNullType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType javaType = new JavaType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetSelfReferencedTypeNullReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    // Test getSelfReferencedType() with self-reference set

    @Test(expected = ClassCastException.class)
    public void testGetSelfReferencedTypeDifferentClass() {
        JavaType javaType = new JavaType(Integer.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(javaType);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericSignatureEmptyStringBuilder() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actual);
    }

    // Test other methods with empty StringBuilder

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        JavaType javaType = new JavaType(String.class);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType3.setReference(javaType);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }

    // Test equals() with different classes

    @Test
    public void testHashCode() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(resolvedRecursiveType1.hashCode(), resolvedRecursiveType2.hashCode());
        JavaType javaType = new JavaType(String.class);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType3.setReference(javaType);
        assertNotEquals(resolvedRecursiveType1.hashCode(), resolvedRecursiveType3.hashCode());
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(List.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    // Test other methods
}