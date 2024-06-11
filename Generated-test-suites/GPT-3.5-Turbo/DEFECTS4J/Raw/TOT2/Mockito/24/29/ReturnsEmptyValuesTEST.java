import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]); // for toString()

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[1]); // for compareTo()

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
    }

    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<Object> result = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueForSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> result = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueForSortedSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueForMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueForLinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
    }

    @Test
    public void testCompleteReturnValueFor() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertNotNull(returnsEmptyValues.returnValueFor(Collection.class));
        assertNotNull(returnsEmptyValues.returnValueFor(Set.class));
        assertNotNull(returnsEmptyValues.returnValueFor(SortedSet.class));
        assertNotNull(returnsEmptyValues.returnValueFor(Map.class));
        assertNotNull(returnsEmptyValues.returnValueFor(HashMap.class));
        assertNotNull(returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }

}