import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[0]);
        assertEquals("Mock for InvocationOnMock, hashCode: 0", returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object argument = new Object();
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[1]);
        when(invocation.getArguments()).thenReturn(new Object[] {argument});
        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForPrimitive() {
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
    }
    
    @Test
    public void testReturnValueForCollection() {
        Collection<Object> collection = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertTrue(collection instanceof LinkedList);
    }
    
    @Test
    public void testReturnValueForSet() {
        Set<Object> set = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(set);
        assertTrue(set instanceof HashSet);
    }
    
    @Test
    public void testReturnValueForMap() {
        Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(map);
        assertTrue(map instanceof HashMap);
    }

    @Test
    public void testReturnValueForSortedSet() {
        SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(sortedSet);
        assertTrue(sortedSet instanceof TreeSet);
    }
    
    @Test
    public void testReturnValueForSortedMap() {
        SortedMap<Object, Object> sortedMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(sortedMap);
        assertTrue(sortedMap instanceof TreeMap);
    }

    @Test
    public void testReturnValueForOtherTypes() {
        assertNull(returnsEmptyValues.returnValueFor(Double.class));
    }
}