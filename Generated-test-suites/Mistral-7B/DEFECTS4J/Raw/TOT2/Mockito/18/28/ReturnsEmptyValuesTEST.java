package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        String returnValue = returnsEmptyValues.answer(invocation);

        assertNotNull(returnValue);

        verify(invocation).getMock();
        verify(invocation).getMethod();
        verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testAnswerForNonToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> nonToStringMethod = String[].class; // Array type is not a method, but this will test the edge case

        Object returnValue = returnsEmptyValues.answer(invocation);

        assertNull(returnValue);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testReturnValueForCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        for (Class<?> collectionType : new Class[] { ArrayList.class, LinkedList.class, HashSet.class, TreeSet.class, LinkedHashSet.class }) {
            Object returnValue = returnsEmptyValues.returnValueFor(collectionType);

            if (Collection.class.isAssignableFrom(collectionType)) {
                assertTrue(returnValue instanceof Collection);
            }

            if (collectionType == ArrayList.class) {
                assertTrue(returnValue instanceof ArrayList);
            }

            if (collectionType == LinkedList.class) {
                assertTrue(returnValue instanceof LinkedList);
            }

            if (collectionType == HashSet.class) {
                assertTrue(returnValue instanceof HashSet);
            }

            if (collectionType == TreeSet.class) {
                assertTrue(returnValue instanceof TreeSet);
            }

            if (collectionType == LinkedHashSet.class) {
                assertTrue(returnValue instanceof LinkedHashSet);
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullMethodObject() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock nullInvocation = null;

        returnsEmptyValues.answer(nullInvocation);
    }

    @Test
    public void testReturnValueForInvalidClassType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> invalidClassType = int[].class; // Two-dimensional arrays are not valid types

        returnsEmptyValues.returnValueFor(invalidClassType);
    }

    @Test
    public void testReturnValueForMethodsThatReturnPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        for (Class<?> primitiveType : new Class<?>[] { int.class, float.class, double.class, boolean.class, char.class, byte.class, short.class, long.class }) {
            Object returnValue = returnsEmptyValues.returnValueFor(primitiveType);
            Object primitiveDefaultValue = Primitives.defaultValueForPrimitiveOrWrapper(primitiveType);

            assertEquals(primitiveDefaultValue, returnValue);
        }
    }

    @Test
    public void testReturnValueForStringType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object returnValue = returnsEmptyValues.returnValueFor(String.class);

        assertTrue(returnValue instanceof String);
        assertEquals("", (String) returnValue);
    }

    @Test
    public void testReturnValueForNullType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object returnValue = returnsEmptyValues.returnValueFor(Null.class);

        assertNull(returnValue);
    }

    @Test
    public void testReturnValueForOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Class<?> otherType = Object.class;

        Object returnValue = returnsEmptyValues.returnValueFor(otherType);

        assertNull(returnValue);
    }
}