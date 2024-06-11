import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        MockName mockName = new MockName();
        mockName.setDefault(true);
        Object mock = new Object();
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class),
                new Object[]{});
        mockName.setMockName(mock.getClass().getSimpleName());
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testCompareTo() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class),
                new Object[]{mock});
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testPrimitive() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Class<?> returnType = int.class;
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(0, returnsEmptyValues.answer(invocation));

        returnType = Integer.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testCollection() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Class<?> returnType = Collection.class;
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        LinkedList<Object> expectedList = new LinkedList<>();
        assertEquals(expectedList, returnsEmptyValues.answer(invocation));

        returnType = Set.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        HashSet<Object> expectedSet = new HashSet<>();
        assertEquals(expectedSet, returnsEmptyValues.answer(invocation));

        returnType = HashSet.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedSet, returnsEmptyValues.answer(invocation));

        returnType = SortedSet.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        TreeSet<Object> expectedSortedSet = new TreeSet<>();
        assertEquals(expectedSortedSet, returnsEmptyValues.answer(invocation));

        returnType = TreeSet.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedSortedSet, returnsEmptyValues.answer(invocation));

        returnType = LinkedHashSet.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        LinkedHashSet<Object> expectedLinkedHashSet = new LinkedHashSet<>();
        assertEquals(expectedLinkedHashSet, returnsEmptyValues.answer(invocation));

        returnType = List.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedList, returnsEmptyValues.answer(invocation));

        returnType = LinkedList.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedList, returnsEmptyValues.answer(invocation));

        returnType = ArrayList.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        ArrayList<Object> expectedArrayList = new ArrayList<>();
        assertEquals(expectedArrayList, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Class<?> returnType = Map.class;
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        HashMap<Object, Object> expectedHashMap = new HashMap<>();
        assertEquals(expectedHashMap, returnsEmptyValues.answer(invocation));

        returnType = HashMap.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedHashMap, returnsEmptyValues.answer(invocation));

        returnType = SortedMap.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        TreeMap<Object, Object> expectedTreeMap = new TreeMap<>();
        assertEquals(expectedTreeMap, returnsEmptyValues.answer(invocation));

        returnType = TreeMap.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertEquals(expectedTreeMap, returnsEmptyValues.answer(invocation));

        returnType = LinkedHashMap.class;
        invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        LinkedHashMap<Object, Object> expectedLinkedHashMap = new LinkedHashMap<>();
        assertEquals(expectedLinkedHashMap, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testOthers() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Class<?> returnType = Object.class;
        InvocationOnMock invocation = new InvocationOnMock(mock, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class),
                new Object[]{returnType});
        assertNull(returnsEmptyValues.answer(invocation));
    }

}