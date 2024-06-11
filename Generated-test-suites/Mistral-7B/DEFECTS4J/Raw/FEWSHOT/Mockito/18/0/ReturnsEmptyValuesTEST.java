package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.mockito3.Mockito.mock;
import static org.mockito.mockito3.Mockito.verify;

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
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test class for class {@link ReturnsEmptyValues}.
 */
public class ReturnsEmptyValuesTest {

    /**
     * Test for method {@link ReturnsEmptyValues#answer(InvocationOnMock)}.
     */
    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock.mockingDetails().getInvocationCalls().get(0);

        // test for toString() method
        assertEquals("Mock for java.lang.String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation));

        // test for compareTo() method
        String mockString = mock(String.class);
        String otherString = mock(String.class);
        verify(mockString).compareTo(otherString);
        verify(otherString).compareTo(mockString);
        ArgumentMatcher<Integer> integerMatcher = arg -> arg != null && arg.equals(0);
        verify(mockString).compareTo(otherString, integerMatcher);
        verify(otherString).compareTo(mockString, integerMatcher);

        // test for other methods
        assertEquals(Integer.TYPE, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(Integer.class, returnsEmptyValues.returnValueFor(Integer.TYPE));
        Collection<String> collection = returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertEquals(0, collection.size());
        Set<String> set = returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(set);
        assertEquals(0, set.size());
        SortedSet<String> sortedSet = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(sortedSet);
        assertEquals(0, sortedSet.size());
        Map<String, String> map = returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(map);
        assertEquals(0, map.size());
        List<String> list = returnsEmptyValues.returnValueFor(List.class);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}