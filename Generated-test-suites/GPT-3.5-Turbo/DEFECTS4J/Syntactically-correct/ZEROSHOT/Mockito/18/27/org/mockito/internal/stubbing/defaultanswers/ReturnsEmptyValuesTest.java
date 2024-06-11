import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.stubbing.Answer;
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

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_toStringMethod() {
        Object mock = new Object();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isToStringMethod() {
                return true;
            }

            @Override
            public Object getArgument(int index) {
                return null;
            }

            @Override
            public boolean isTopLevelRunnable() {
                return false;
            }

            @Override
            public boolean isToString() {
                return true;
            }

            @Override
            public boolean isEqualsMethod() {
                return false;
            }

            @Override
            public boolean returnsPrimitive() {
                return false;
            }

            @Override
            public boolean hasInvocationSequence() {
                return false;
            }

            @Override
            public boolean isSetter() {
                return false;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public Integer getSequenceNumber() {
                return null;
            }

            @Override
            public Integer getCount() {
                return null;
            }

            @Override
            public Answer<Object> getDefaultAnswer() {
                return null;
            }

            @Override
            public InvocationOnMockSequence getInvocationSequence() {
                return null;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public MockName getMockName() {
                return null;
            }

            @Override
            public Throwable getLastThrowable() {
                return null;
            }
        };

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object mock = new Object();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public Object[] getArguments() {
                return new Object[] { mock };
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isToStringMethod() {
                return false;
            }

            @Override
            public Object getArgument(int index) {
                return null;
            }

            @Override
            public boolean isTopLevelRunnable() {
                return false;
            }

            @Override
            public boolean isToString() {
                return false;
            }

            @Override
            public boolean isEqualsMethod() {
                return false;
            }

            @Override
            public boolean returnsPrimitive() {
                return false;
            }

            @Override
            public boolean hasInvocationSequence() {
                return false;
            }

            @Override
            public boolean isSetter() {
                return false;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public Integer getSequenceNumber() {
                return null;
            }

            @Override
            public Integer getCount() {
                return null;
            }

            @Override
            public Answer<Object> getDefaultAnswer() {
                return null;
            }

            @Override
            public InvocationOnMockSequence getInvocationSequence() {
                return null;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public MockName getMockName() {
                return null;
            }

            @Override
            public Throwable getLastThrowable() {
                return null;
            }
        };

        Object result = returnsEmptyValues.answer(invocation);

        assertNotNull(result);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueForPrimitiveOrWrapper() {
        Class<?> primitiveType = int.class;
        Class<?> wrapperType = Integer.class;

        Object primitiveResult = returnsEmptyValues.returnValueFor(primitiveType);
        Object wrapperResult = returnsEmptyValues.returnValueFor(wrapperType);

        assertNotNull(primitiveResult);
        assertNotNull(wrapperResult);
        assertEquals(0, primitiveResult);
        assertEquals(0, wrapperResult);
    }

    @Test
    public void testAnswer_returnValueForCollection() {
        Class<?> collectionType = Collection.class;

        Object result = returnsEmptyValues.returnValueFor(collectionType);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForSet() {
        Class<?> setType = Set.class;

        Object result = returnsEmptyValues.returnValueFor(setType);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForHashSet() {
        Class<?> hashSetType = HashSet.class;

        Object result = returnsEmptyValues.returnValueFor(hashSetType);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForSortedSet() {
        Class<?> sortedSetType = SortedSet.class;

        Object result = returnsEmptyValues.returnValueFor(sortedSetType);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForTreeSet() {
        Class<?> treeSetType = TreeSet.class;

        Object result = returnsEmptyValues.returnValueFor(treeSetType);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForLinkedHashSet() {
        Class<?> linkedHashSetType = LinkedHashSet.class;

        Object result = returnsEmptyValues.returnValueFor(linkedHashSetType);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForList() {
        Class<?> listType = List.class;

        Object result = returnsEmptyValues.returnValueFor(listType);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForLinkedList() {
        Class<?> linkedListType = LinkedList.class;

        Object result = returnsEmptyValues.returnValueFor(linkedListType);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForArrayList() {
        Class<?> arrayListType = ArrayList.class;

        Object result = returnsEmptyValues.returnValueFor(arrayListType);

        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForMap() {
        Class<?> mapType = Map.class;

        Object result = returnsEmptyValues.returnValueFor(mapType);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForHashMap() {
        Class<?> hashMapType = HashMap.class;

        Object result = returnsEmptyValues.returnValueFor(hashMapType);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForSortedMap() {
        Class<?> sortedMapType = SortedMap.class;

        Object result = returnsEmptyValues.returnValueFor(sortedMapType);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForTreeMap() {
        Class<?> treeMapType = TreeMap.class;

        Object result = returnsEmptyValues.returnValueFor(treeMapType);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForLinkedHashMap() {
        Class<?> linkedHashMapType = LinkedHashMap.class;

        Object result = returnsEmptyValues.returnValueFor(linkedHashMapType);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_returnValueForOtherType() {
        Class<?> otherType = Object.class;

        Object result = returnsEmptyValues.returnValueFor(otherType);

        assertNull(result);
    }
}