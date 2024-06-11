import org.junit.Test;
import org.junit.internal.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer<String> answer = Answers.callsRealMethod();
        when(mock.toString()).then(answer);

        String result = returnsEmptyValues.answer(new InvocationOnMock(mock, "toString()", new Object[0]));

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock = mock(Comparable.class);
        when(mock.compareTo(any(Comparable.class))).thenReturn(1);

        int result = (int) returnsEmptyValues.answer(new InvocationOnMock(mock, "compareTo(T)", new Object[]{mock}));

        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveTypes() {
        for (Class<?> primitiveType : new Class[]{boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class}) {
            Object result = returnsEmptyValues.answer(new InvocationOnMock(null, "get" + primitiveType.getSimpleName(), new Object[0]));
            assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(primitiveType), result);
        }
    }

    @Test
    public void testCollectionTypes_EmptyList() {
        for (Class<?> collectionType : new Class[]{Collection.class, List.class, ArrayList.class, LinkedList.class}) {
            Object result = returnsEmptyValues.answer(new InvocationOnMock(null, "getValue" + collectionType.getSimpleName(), new Object[0]));
            assertTrue(result instanceof Collection);
            assertTrue(result instanceof List);
            assertTrue(result instanceof ArrayList);
            assertTrue(result instanceof LinkedList);
            assertEquals(0, ((Collection) result).size());
        }
    }

    @Test
    public void testCollectionTypes_HashSet() {
        for (Class<?> collectionType : new Class[]{Set.class, HashSet.class, LinkedHashSet.class, TreeSet.class}) {
            Object result = returnsEmptyValues.answer(new InvocationOnMock(null, "getValue" + collectionType.getSimpleName(), new Object[0]));
            assertTrue(result instanceof Collection);
            assertTrue(result instanceof Set);
            assertTrue(result instanceof HashSet);
            assertTrue(result instanceof LinkedHashSet);
            assertTrue(result instanceof TreeSet);
            assertEquals(0, ((Collection) result).size());
        }
    }

    @Test
    public void testCollectionTypes_TreeMap() {
        Class<SortedMap> sortedMapType = SortedMap.class;
        Class<TreeMap> treeMapType = TreeMap.class;

        Object result = returnsEmptyValues.answer(new InvocationOnMock(null, "getValue" + sortedMapType.getSimpleName(), new Object[0]));
        assertTrue(result instanceof Collection);
        assertTrue(result instanceof Map);
        assertTrue(result instanceof SortedMap);
        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((Collection) result).size());

        when(returnsEmptyValues.answer(new InvocationOnMock(null, "getValue" + treeMapType.getSimpleName(), new Object[0]))).thenReturn(new TreeMap<Object, Object>());
        verify(returnsEmptyValues).answer(new InvocationOnMock(null, "getValue" + treeMapType.getSimpleName(), new Object[0]));
    }
}