import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void answer_toToStringMethod_shouldReturnStringDescription() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        String expectedDescription = "Mock for Object, hashCode: " + invocation.getMock().hashCode();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedDescription, result);
    }

    @Test
    public void answer_toCompareToMethod_withSameReferences_shouldReturnZero() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(arguments[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void answer_toCompareToMethod_withDifferentReferences_shouldReturnNonZero() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(new Object());

        Object result = returnsEmptyValues.answer(invocation);

        assertNotEquals(0, result);
    }

    @Test
    public void answer_toPrimitiveReturnType_shouldReturnDefaultPrimitiveValue() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Primitives.class.getMethod("defaultValueForPrimitiveOrWrapper", Class.class));
        when(invocation.getMock()).thenReturn(mock(Primitives.class));
        Class<?> returnType = invocation.getMethod().getReturnType();
        Object expectedValue = Primitives.defaultValueForPrimitiveOrWrapper(returnType);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toCollectionReturnType_shouldReturnEmptyLinkedList() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = Collection.class;
        Object expectedValue = new LinkedList<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toSetReturnType_shouldReturnEmptyHashSet() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = Set.class;
        Object expectedValue = new HashSet<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toHashSetReturnType_shouldReturnEmptyHashSet() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = HashSet.class;
        Object expectedValue = new HashSet<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toSortedSetReturnType_shouldReturnEmptyTreeSet() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = SortedSet.class;
        Object expectedValue = new TreeSet<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toTreeSetReturnType_shouldReturnEmptyTreeSet() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = TreeSet.class;
        Object expectedValue = new TreeSet<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toLinkedHashSetReturnType_shouldReturnEmptyLinkedHashSet() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = LinkedHashSet.class;
        Object expectedValue = new LinkedHashSet<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toListReturnType_shouldReturnEmptyLinkedList() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = List.class;
        Object expectedValue = new LinkedList<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toLinkedListReturnType_shouldReturnEmptyLinkedList() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = LinkedList.class;
        Object expectedValue = new LinkedList<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toArrayListReturnType_shouldReturnEmptyArrayList() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = ArrayList.class;
        Object expectedValue = new ArrayList<Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toMapReturnType_shouldReturnEmptyHashMap() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = Map.class;
        Object expectedValue = new HashMap<Object, Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toHashMapReturnType_shouldReturnEmptyHashMap() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = HashMap.class;
        Object expectedValue = new HashMap<Object, Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toSortedMapReturnType_shouldReturnEmptyTreeMap() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = SortedMap.class;
        Object expectedValue = new TreeMap<Object, Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toTreeMapReturnType_shouldReturnEmptyTreeMap() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = TreeMap.class;
        Object expectedValue = new TreeMap<Object, Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toLinkedHashMapReturnType_shouldReturnEmptyLinkedHashMap() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = LinkedHashMap.class;
        Object expectedValue = new LinkedHashMap<Object, Object>();

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(expectedValue, result);
    }

    @Test
    public void answer_toOtherReturnType_shouldReturnNull() throws NoSuchMethodException {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMock()).thenReturn(mock(ReturnsEmptyValues.class));
        Class<?> returnType = Override.class;

        Object result = returnsEmptyValues.answer(invocation);

        assertNull(result);
    }

}