import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer<String> answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        String str = (String) mock.toString();
        assertEquals("Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode(), str);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Answer<Integer> answer = new ReturnsEmptyValues();
        when(mock1.equals(mock2)).thenReturn(true);
        when(mock1.compareTo(mock2)).thenAnswer(answer);

        int result = mock1.compareTo(mock2);
        assertEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyEmptyCollection() {
        Collection<Object> collection = new ReturnsEmptyValues().answer(Mockito.mock(InvocationOnMock.class));
        collection.add(new Object());
    }

    @Test
    public void testEmptyCollectionTypes() {
        for (Class<?> collectionType : new Class[] { Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
            Collection<?> collection = new ReturnsEmptyValues().answer(Mockito.mock(InvocationOnMock.class, new Class[] { collectionType }));
            assertNotNull(collection);
            assertEquals(0, collection.size());
        }
    }

    @Test
    public void testEmptyMapTypes() {
        for (Class<?> mapType : new Class[] { Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
            Map<?, ?> map = new ReturnsEmptyValues().answer(Mockito.mock(InvocationOnMock.class, new Class[] { mapType }));
            assertNotNull(map);
            assertEquals(0, map.size());
        }
    }

    @Test
    public void testPrimitiveTypes() {
        for (Class<?> primitiveType : new Class[] { byte.class, short.class, int.class, long.class, char.class, boolean.class, float.class, double.class, Byte.class, Short.class, Integer.class, Long.class, Character.class, Boolean.class, Float.class, Double.class }) {
            Answer<Object> answer = new ReturnsEmptyValues();
            Object mock = Mockito.mock(primitiveType);

            Object result = answer.answer(Mockito.mock(InvocationOnMock.class, new Class[] { primitiveType }));
            if (primitiveType.isPrimitive()) {
                assertEquals(primitiveType.getPrimitiveClass().getName().substring(0, primitiveType.getName().length() - 3), result.getClass().getName());
            } else {
                assertEquals(primitiveType.getName().substring(0, primitiveType.getName().length() - 3), result.toString());
            }
        }
    }

    @Test
    public void testNullReturnType() {
        Answer<Object> answer = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Object.class);
        Object result = answer.answer(Mockito.mock(InvocationOnMock.class, new Class[] { Object.class }));
        verifyZeroInteractions(mock);
        assertEquals(null, result);
    }
}