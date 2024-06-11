package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        // Given
        Object mock = Mockito.mock(String.class);

        // When
        Answer answer = new ReturnsEmptyValues();
        String result = (String) answer.answer(Mockito.argThat(invocation -> methodsGuru.isToString(invocation.getMethod())));

        // Then
        verifyZeroInteractions(mock);
        assertNotNull(result);
        assertTrue(result.contains("Mock for"));
    }

    @Test
    public void testCompareToMethod() {
        // Given
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        // When
        Answer answer = new ReturnsEmptyValues();

        // Then
        when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);
        int result1 = (int) mock1.compareTo(mock2);
        int result2 = (int) mock2.compareTo(mock1);

        // Verify
        verifyNoMoreInteractions(mock1, mock2);
        assertEquals(1, result1);
        assertEquals(-1, result2);
    }

    @Test
    public void testPrimitiveTypes() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        byte byteValue = (byte) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == byte.class));
        short shortValue = (short) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == short.class));
        int intValue = (int) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == int.class));
        long longValue = (long) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == long.class));
        float floatValue = (float) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == float.class));
        double doubleValue = (double) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == double.class));
        boolean booleanValue = (boolean) answer.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() == boolean.class));

        // Then
        verifyNoMoreInteractions(answer.mockUtil, answer.methodsGuru);
        assertEquals((byte) 0, byteValue);
        assertEquals((short) 0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals(0.0f, floatValue, 0.001f);
        assertEquals(0.0d, doubleValue, 0.001d);
        assertEquals(false, booleanValue);
    }

    @Test
    public void testCollectionTypes() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Collection<String> collectionValue = (Collection<String>) answer.answer(Mockito.argThat(invocation -> Collection.class.isAssignableFrom(invocation.getMethod().getReturnType())));

        // Then
        verifyNoMoreInteractions(answer.mockUtil, answer.methodsGuru);
        assertTrue(collectionValue.isEmpty());
    }

    @Test
    public void testSetTypes() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Set<String> setValue = (Set<String>) answer.answer(Mockito.argThat(invocation -> Set.class.isAssignableFrom(invocation.getMethod().getReturnType())));

        // Then
        verifyNoMoreInteractions(answer.mockUtil, answer.methodsGuru);
        assertTrue(setValue.isEmpty());
    }

    @Test
    public void testMapTypes() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Map<String, String> mapValue = (Map<String, String>) answer.answer(Mockito.argThat(invocation -> Map.class.isAssignableFrom(invocation.getMethod().getReturnType())));

        // Then
        verifyNoMoreInteractions(answer.mockUtil, answer.methodsGuru);
        assertTrue(mapValue.isEmpty());
    }
}