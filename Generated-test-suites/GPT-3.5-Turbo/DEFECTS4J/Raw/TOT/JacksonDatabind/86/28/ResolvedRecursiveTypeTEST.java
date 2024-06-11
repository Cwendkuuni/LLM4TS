package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType(getClass());
        resolvedRecursiveType = new ResolvedRecursiveType(getClass(), null);
    }

    @Test
    public void testSetReference_WhenCalled_SetsReferencedType() {
        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_WhenReferenceAlreadySet_ThrowsIllegalStateException() {
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(referencedType);
    }

    @Test
    public void testGetGenericSignature_WhenCalled_DelegatesToReferencedType() {
        StringBuilder sb = new StringBuilder();
        referencedType.getGenericSignature(sb);

        assertEquals(sb, resolvedRecursiveType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature_WhenCalled_DelegatesToReferencedType() {
        StringBuilder sb = new StringBuilder();
        referencedType.getErasedSignature(sb);

        assertEquals(sb, resolvedRecursiveType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testWithContentType_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(referencedType));
    }

    @Test
    public void testWithTypeHandler_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(this));
    }

    @Test
    public void testWithContentTypeHandler_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(this));
    }

    @Test
    public void testWithValueHandler_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(this));
    }

    @Test
    public void testWithContentValueHandler_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(this));
    }

    @Test
    public void testWithStaticTyping_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow_WhenCalled_ReturnsSelf() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(getClass()));
    }
    
    @Test
    public void testRefine_WhenCalled_ReturnsNull() {
        assertEquals(null, resolvedRecursiveType.refine(getClass(), null, null, null));
    }
    
    @Test
    public void testIsContainerType_WhenCalled_ReturnsFalse() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString_WhenReferencedTypeIsNull_ReturnsUNRESOLVED() {
        resolvedRecursiveType.setReference(null);
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testToString_WhenReferencedTypeIsNotNull_ReturnsReferencedTypeClass() {
        assertEquals("[recursive type; " + getClass().getName() + "]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEquals_WhenObjectIsSame_ReturnsTrue() {
        assertEquals(true, resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test 
    public void testEquals_WhenObjectIsNull_ReturnsFalse() {
        assertNotEquals(true, resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEquals_WhenReferencedTypeIsNull_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(getClass(), null);
        assertNotEquals(true, resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEquals_WhenReferencedTypesAreDifferent_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(getClass(), null);
        resolvedRecursiveType.setReference(referencedType);
        other.setReference(new JavaType(Object.class));
        
        assertNotEquals(true, resolvedRecursiveType.equals(other));
    }
}