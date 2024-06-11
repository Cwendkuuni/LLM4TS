
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.Primitives;
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

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testToString() {
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
            public int getArgumentCount() {
                return 0;
            }

            @Override
            public Object getArgument(int index) {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isAnswered() {
                return false;
            }
        };

        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testCompareTo() {
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public int getArgumentCount() {
                return 0;
            }

            @Override
            public Object getArgument(int index) {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isAnswered() {
                return false;
            }
        };

        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForPrimitiveInt() {
        Class<Integer> type = Integer.TYPE;
        assertEquals(0, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForWrapperInteger() {
        Class<Integer> type = Integer.class;
        assertEquals(0, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForCollection() {
        Class<Collection> type = Collection.class;
        List<Object> expected = new LinkedList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForSet() {
        Class<Set> type = Set.class;
        Set<Object> expected = new HashSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForHashSet() {
        Class<HashSet> type = HashSet.class;
        Set<Object> expected = new HashSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForSortedSet() {
        Class<SortedSet> type = SortedSet.class;
        Set<Object> expected = new TreeSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForTreeSet() {
        Class<TreeSet> type = TreeSet.class;
        Set<Object> expected = new TreeSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForLinkedHashSet() {
        Class<LinkedHashSet> type = LinkedHashSet.class;
        Set<Object> expected = new LinkedHashSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForList() {
        Class<List> type = List.class;
        List<Object> expected = new LinkedList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForLinkedList() {
        Class<LinkedList> type = LinkedList.class;
        List<Object> expected = new LinkedList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForArrayList() {
        Class<ArrayList> type = ArrayList.class;
        List<Object> expected = new ArrayList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForMap() {
        Class<Map> type = Map.class;
        Map<Object, Object> expected = new HashMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForHashMap() {
        Class<HashMap> type = HashMap.class;
        Map<Object, Object> expected = new HashMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForSortedMap() {
        Class<SortedMap> type = SortedMap.class;
        Map<Object, Object> expected = new TreeMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForTreeMap() {
        Class<TreeMap> type = TreeMap.class;
        Map<Object, Object> expected = new TreeMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testReturnValueForLinkedHashMap() {
        Class<LinkedHashMap> type = LinkedHashMap.class;
        Map<Object, Object> expected = new LinkedHashMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }

    // TODO: Add testReturnValueForIterable

    // TODO: Add tests for the rest of collection types

}
