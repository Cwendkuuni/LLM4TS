package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethodReturnDescription() {
        Object mock = mock(String.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        assertEquals("Mock for String, hashCode: 153481531", mock.toString());
        verify(mock).toString();
    }

    @Test
    public void testCompareToMethodReturnsOne() {
        Object mock1 = mock(Comparable.class, Answers.CALLS_REAL_METHODS);
        Object mock2 = mock(Comparable.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testPrimitiveReturningMethods() {
        Object mock = mock(int[].class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();

        when(mock.length).thenAnswer(answer);

        int[] array = new int[0];
        assertEquals(0, array.length);
        assertEquals(0, ((Answer) mock.length).answer(invocation(mock, anyInt(), anyInt())).hashCode());
    }

    @Test
    public void testCollectionReturningMethods() {
        Object mock = mock(Collection.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();

        when(mock.size()).thenAnswer(answer);
        when(mock.isEmpty()).thenAnswer(answer);
        when(mock.iterator()).thenAnswer(answer);

        List<Object> collection = new ArrayList<>();
        assertEquals(0, ((Collection) mock).size());
        assertEquals(true, ((Collection) mock).isEmpty());
        assertNotNull(((Iterator) mock.iterator()).hasNext());

        verify(mock).size();
        verify(mock).isEmpty();
        verify(mock).iterator();
    }

    @Test
    public void testSetReturningMethods() {
        Object mock = mock(Set.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();

        when(mock.size()).thenAnswer(answer);
        when(mock.isEmpty()).thenAnswer(answer);
        when(mock.contains(any())).thenAnswer(answer);
        when(mock.add(any())).thenAnswer(answer);

        Set<Object> set = new HashSet<>();
        assertEquals(0, ((Set) mock).size());
        assertEquals(true, ((Set) mock).isEmpty());
        assertFalse(((Set) mock).contains(new Object()));
        assertTrue(((Set) mock).add(new Object()));
        assertEquals(1, ((Set) mock).size());

        verify(mock).size();
        verify(mock).isEmpty();
        verify(mock).contains(any());
        verify(mock).add(any());
    }

    @Test
    public void testMapReturningMethods() {
        Object mock = mock(Map.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();

        when(mock.size()).thenAnswer(answer);
        when(mock.isEmpty()).thenAnswer(answer);
        when(mock.containsKey(any())).thenAnswer(answer);
        when(mock.containsValue(any())).thenAnswer(answer);
        when(mock.get(any())).thenAnswer(answer);

        Map<Object, Object> map = new HashMap<>();
        assertEquals(0, ((Map) mock).size());
        assertEquals(true, ((Map) mock).isEmpty());
        assertFalse(((Map) mock).containsKey(new Object()));
        assertFalse(((Map) mock).containsValue(new Object()));
        assertEquals(null, ((Map) mock).get(new Object()));

        verify(mock).size();
        verify(mock).isEmpty();
        verify(mock).containsKey(any());
        verify(mock).containsValue(any());
        verify(mock).get(any());
    }
}