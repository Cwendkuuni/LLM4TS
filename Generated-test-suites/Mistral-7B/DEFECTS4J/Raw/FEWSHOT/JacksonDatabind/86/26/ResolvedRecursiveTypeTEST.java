package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBindings;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _mockType;
    private ResolvedRecursiveType _testee;

    @Before
    public void setUp() {
        _mockType = mock(JavaType.class);
        _testee = new ResolvedRecursiveType(Object.class, TypeBindings.empty());
    }

    @Test
    public void testSetReference() {
        JavaType refType = mock(JavaType.class);
        _testee.setReference(refType);
        assertSame(refType, _testee.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType1 = mock(JavaType.class);
        JavaType refType2 = mock(JavaType.class);
        _testee.setReference(refType1);
        _testee.setReference(refType2);
    }

    @Test
    public void testEquality() {
        JavaType other = new ResolvedRecursiveType(String.class, TypeBindings.empty());
        assertNull(other.getSelfReferencedType());
        assertFalse(_testee.equals(other));

        JavaType otherWithSelfRef = new ResolvedRecursiveType(Object.class, TypeBindings.empty()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _mockType;
            }
        };
        when(_mockType.equals(_mockType)).thenReturn(true);
        assertTrue(_testee.equals(otherWithSelfRef));
    }

    @Test
    public void testGetGenericSignature() {
        TypeSerializerBindings typeSerializerBindings = mock(TypeSerializerBindings.class);
        when(typeSerializerBindings.getSerializerInstance(_mockType)).thenReturn(mock(Object.class));

        StringBuilder expectedResult = new StringBuilder("[recursive type; ");
        expectedResult.append(_mockType.getRawClass().getName());
        expectedResult.append("]");

        StringBuilder result = new StringBuilder(40);
        _testee.getGenericSignature(result);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expectedResult = new StringBuilder("Ljava/lang/Object;");
        StringBuilder result = new StringBuilder(40);
        _testee.getErasedSignature(result);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(_testee.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder expectedResult = new StringBuilder("[recursive type; ");
        expectedResult.append(Object.class.getName());
        expectedResult.append("]");

        StringBuilder result = new StringBuilder(40);
        _testee.toString(result);
        assertEquals(expectedResult.toString(), result.toString());
    }
}