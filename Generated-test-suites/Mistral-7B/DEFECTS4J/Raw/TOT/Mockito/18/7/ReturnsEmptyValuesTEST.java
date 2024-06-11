package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);
        when(returnsEmptyValues.answer(any(InvocationOnMock.class)))
                .thenAnswer(invocation -> invocation.getMock().toString());

        Object mock = Mockito.mock(String.class);
        MockName name = MockUtil.getMockName(mock);

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(new InvocationOnMock(mock, null)));
        assertEquals(name.toString(), returnsEmptyValues.answer(new InvocationOnMock(mock, new Object[]{})));
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        when(returnsEmptyValues.answer(any(InvocationOnMock.class)))
                .thenAnswer(invocation -> invocation.getMock() == invocation.getArguments()[0] ? 0 : 1);

        verify(mock1, times(1)).compareTo(mock2);
        verify(mock2, times(1)).compareTo(mock1);
        assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(null, new Object[]{mock1, mock2})));
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        when(returnsEmptyValues.answer(any(InvocationOnMock.class)))
                .thenAnswer(invocation -> Primitives.defaultValueForPrimitiveOrWrapper(invocation.getMethod().getReturnType()));

        Object intMock = Mockito.mock(int.class);
        Object booleanMock = Mockito.mock(boolean.class);

        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(intMock, null)));
        assertEquals(false, returnsEmptyValues.answer(new InvocationOnMock(booleanMock, null)));
        // edge case: null return type
        assertNull(returnsEmptyValues.answer(new InvocationOnMock(null, null)));
        // error case: incorrect return type
        assertNull(returnsEmptyValues.answer(new InvocationOnMock(new Object(), null)));
    }

    @Test
    public void testCollections() {
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        when(returnsEmptyValues.answer(any(InvocationOnMock.class)))
                .thenAnswer(invocation -> {
                    Class<?> type = invocation.getMethod().getReturnType();
                    if (Collection.class.isAssignableFrom(type)) {
                        return new LinkedList<>();
                    }
                    return null;
                });

        Object collectionMock = Mockito.mock(Collection.class);

        assertNotNull(returnsEmptyValues.answer(new InvocationOnMock(collectionMock, null)));
        assertNotNull(returnsEmptyValues.answer(new InvocationOnMock(collectionMock, new Object[0])));

        List<String> listMock = Mockito.mock(List.class);
        assertEquals(Collections.emptyList(), returnsEmptyValues.answer(new InvocationOnMock(listMock, null)));
        assertEquals(Collections.emptyList(), returnsEmptyValues.answer(new InvocationOnMock(listMock, new Object[0])));

        Set<String> setMock = Mockito.mock(Set.class);
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(setMock, null)));
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(setMock, new Object[0])));

        HashSet<String> hashSetMock = Mockito.mock(HashSet.class);
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(hashSetMock, null)));
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(hashSetMock, new Object[0])));

        SortedSet<String> sortedSetMock = Mockito.mock(SortedSet.class);
        assertEquals(Collections.emptySortedSet(), returnsEmptyValues.answer(new InvocationOnMock(sortedSetMock, null)));
        assertEquals(Collections.emptySortedSet(), returnsEmptyValues.answer(new InvocationOnMock(sortedSetMock, new Object[0])));

        TreeSet<String> treeSetMock = Mockito.mock(TreeSet.class);
        assertEquals(Collections.emptySortedSet(), returnsEmptyValues.answer(new InvocationOnMock(treeSetMock, null)));
        assertEquals(Collections.emptySortedSet(), returnsEmptyValues.answer(new InvocationOnMock(treeSetMock, new Object[0])));

        LinkedHashSet<String> linkedHashSetMock = Mockito.mock(LinkedHashSet.class);
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(linkedHashSetMock, null)));
        assertEquals(Collections.emptySet(), returnsEmptyValues.answer(new InvocationOnMock(linkedHashSetMock, new Object[0])));
    }
}