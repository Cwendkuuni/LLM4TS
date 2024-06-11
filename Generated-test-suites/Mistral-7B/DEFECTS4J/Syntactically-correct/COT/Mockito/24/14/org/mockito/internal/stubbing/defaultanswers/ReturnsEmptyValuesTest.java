import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
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

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        Answer<Object> answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);
        String result = mock.toString();
        assertNotNull(result);
        verify(mock).toString();
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result.toString());
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        Answer<Object> answer = new ReturnsEmptyValues();
        when(mock1.compareTo(mock2)).thenAnswer(answer);
        int result = mock1.compareTo(mock2);
        assertEquals(1, result);
        verify(mock1).compareTo(mock2);
    }

    @Test
    public void testReturnValueForPrimitiveOrWrapper() {
        Answer<Object> answer = new ReturnsEmptyValues();

        byte result1 = (byte) answer.returnValueFor(Byte.TYPE);
        short result2 = (short) answer.returnValueFor(Short.TYPE);
        int result3 = (int) answer.returnValueFor(Integer.TYPE);
        long result4 = (long) answer.returnValueFor(Long.TYPE);
        float result5 = (float) answer.returnValueFor(Float.TYPE);
        double result6 = (double) answer.returnValueFor(Double.TYPE);
        boolean result7 = (boolean) answer.returnValueFor(Boolean.TYPE);
        char result8 = (char) answer.returnValueFor(Character.TYPE);

        assertNotNull(result1);
        assertEquals((byte) 0, result1);
        assertNotNull(result2);
        assertEquals((short) 0, result2);
        assertNotNull(result3);
        assertEquals(0, result3);
        assertNotNull(result4);
        assertEquals(0L, result4);
        assertNotNull(result5);
        assertEquals(0.0f, result5, 0.001f);
        assertNotNull(result6);
        assertEquals(0.0d, result6, 0.001d);
        assertNotNull(result7);
        assertEquals(false, result7);
        assertNotNull(result8);
        assertEquals((char) 0, result8);
    }

    @Test
    public void testReturnValueForCollection() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Collection<String> result = (Collection<String>) answer.returnValueFor(Collection.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSet() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Set<String> result1 = (Set<String>) answer.returnValueFor(Set.class);
        Set<String> result2 = (Set<String>) answer.returnValueFor(HashSet.class);
        Set<String> result3 = (Set<String>) answer.returnValueFor(LinkedHashSet.class);
        Set<String> result4 = (Set<String>) answer.returnValueFor(TreeSet.class);

        assertNotNull(result1);
        assertTrue(result1.isEmpty());
        assertNotNull(result2);
        assertTrue(result2 instanceof HashSet<?>);
        assertTrue(result2.isEmpty());
        assertNotNull(result3);
        assertTrue(result3 instanceof LinkedHashSet<?>);
        assertTrue(result3.isEmpty());
        assertNotNull(result4);
        assertTrue(result4 instanceof TreeSet<?>);
        assertTrue(result4.isEmpty());
    }

    @Test
    public void testReturnValueForMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Map<String, String> result = (Map<String, String>) answer.returnValueFor(Map.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSortedMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        SortedMap<String, String> result = (SortedMap<String, String>) answer.returnValueFor(SortedMap.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReturnValueForIterable() {
        Answer<Object> answer = new ReturnsEmptyValues();
        Iterable<String> result = (Iterable<String>) answer.returnValueFor(Iterable.class);
        result.iterator().next();
    }
}