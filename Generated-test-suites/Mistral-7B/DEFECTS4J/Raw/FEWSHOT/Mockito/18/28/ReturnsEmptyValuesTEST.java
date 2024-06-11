package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_WhenInvokeToStringMethod_ThenReturnsDescriptionOfMock() {
        // Given
        Object mock = mock(String.class);

        // When
        Answer answer = new ReturnsEmptyValues();
        String result = (String) answer.answer(new InvocationOnMock(mock, mock.getClass().getMethod("toString")));

        // Then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_WhenInvokeCompareToMethod_ThenReturnsZeroIfEquals_AndNonZeroOtherwise() {
        // Given
        Object mock = mock(Object.class);
        Object otherMock = mock(Object.class);

        // When
        Answer answer = new ReturnsEmptyValues();
        int result = (Integer) answer.answer(new InvocationOnMock(mock, mock.getClass().getMethod("compareTo", Object.class), mock, otherMock));

        // Then
        assertNotNull(result);
        if (mock.equals(otherMock)) {
            assertEquals(0, result);
        } else {
            assertTrue(result > 0);
        }
    }

    @Test
    public void testReturnValueFor_WhenInvokeForPrimitiveType_ThenReturnsDefaultValue() {
        // Given
        Class<Byte> byteType = Byte.TYPE;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Byte result = (Byte) returnsEmptyValues.returnValueFor(byteType);

        // Then
        assertEquals((byte) 0, result);
    }

    @Test
    public void testReturnValueFor_WhenInvokeForCollectionType_ThenReturnsEmptyCollection() {
        // Given
        Class<Collection<String>> collectionType = Collection.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<String> result = (Collection<String>) returnsEmptyValues.returnValueFor(collectionType);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_WhenInvokeForSetType_ThenReturnsEmptySet() {
        // Given
        Class<Set<Integer>> setType = HashSet.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Integer> result = (Set<Integer>) returnsEmptyValues.returnValueFor(setType);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_WhenInvokeForMapType_ThenReturnsEmptyMap() {
        // Given
        Class<Map<String, Integer>> mapType = HashMap.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.returnValueFor(mapType);

        // Then
        assertTrue(result.isEmpty());
    }
}