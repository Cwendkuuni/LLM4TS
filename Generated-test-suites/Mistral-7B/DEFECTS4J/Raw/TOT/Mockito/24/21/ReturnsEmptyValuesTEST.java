import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @InjectMocks
    private ReturnsEmptyValues returnsEmptyValues;

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        String result = returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isToString(invocation.getMethod())));
        MockitoAnnotations.initMocks(this);
        Mockito.verify(mock).toString();
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToZero() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);

        MockitoAnnotations.initMocks(this);

        int result = returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
        verify(mock1).compareTo(mock2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareToPositive() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);

        MockitoAnnotations.initMocks(this);

        Mockito.when(mock1.compareTo(mock2)).thenReturn(5);
        int result = returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
        assertEquals(5, result);
    }

    @Test
    public void testReturnPrimitiveInt() {
        int result = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == int.class));
        assertEquals(0, result);
    }

    @Test
    public void testReturnPrimitiveBoolean() {
        boolean result = (boolean) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == boolean.class));
        assertEquals(false, result);
    }

    @Test
    public void testReturnPrimitiveChar() {
        char result = (char) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == char.class));
        assertEquals('\u0000', result);
    }

    @Test
    public void testReturnBooleanCollectionEmpty() {
        Collection<Boolean> result = (Collection<Boolean>) returnsEmptyValues.answer(Mockito.argThat(invocation -> Collection.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnSetEmpty() {
        Set<String> result = (Set<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> Set.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnArrayListEmpty() {
        List<Integer> result = (List<Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> List.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnHashSetEmpty() {
        Set<Integer> result = (Set<Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> HashSet.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnLinkedHashSetEmpty() {
        Set<Integer> result = (Set<Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> LinkedHashSet.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnTreeSetEmpty() {
        SortedSet<Integer> result = (SortedSet<Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> TreeSet.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnLinkedTreeSetEmpty() {
        SortedSet<Integer> result = (SortedSet<Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> LinkedTreeSet.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnHashMapEmpty() {
        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> Map.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnLinkedHashMapEmpty() {
        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> LinkedHashMap.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnTreeMapEmpty() {
        SortedMap<String, Integer> result = (SortedMap<String, Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> TreeMap.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    public void testReturnLinkedTreeMapEmpty() {
        SortedMap<String, Integer> result = (SortedMap<String, Integer>) returnsEmptyValues.answer(Mockito.argThat(invocation -> LinkedTreeMap.class.isAssignableFrom(invocation.getMethod().getReturnType())));
        assertEquals(0, result.size());
        assertNotNull(result);
    }
}