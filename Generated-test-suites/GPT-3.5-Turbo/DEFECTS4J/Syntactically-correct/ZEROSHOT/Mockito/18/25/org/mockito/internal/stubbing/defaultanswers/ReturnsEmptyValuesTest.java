import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.Mockito;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object mock = new Object();
        Mockito.when(invocation.getMock()).thenReturn(mock);
        MockName name = Mockito.mock(MockName.class);
        Mockito.when(Mockito.mockUtil.getMockName(mock)).thenReturn(name);
        Mockito.when(name.isDefault()).thenReturn(true);
        Mockito.when(Mockito.mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);
        Mockito.when(mock.hashCode()).thenReturn(12345);
        String expected = "Mock for Object, hashCode: 12345";
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testCompareTo() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object mock = new Object();
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Object[] arguments = new Object[1];
        arguments[0] = mock;
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testPrimitiveOrWrapper() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("isToString", Object.class));
        Class<?> type = Integer.class;
        assertEquals(0, returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testCollection() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = Collection.class;
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testSet() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = Set.class;
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testHashSet() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = HashSet.class;
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testSortedSet() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = SortedSet.class;
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testTreeSet() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = TreeSet.class;
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testLinkedHashSet() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = LinkedHashSet.class;
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testList() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = List.class;
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testLinkedList() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = LinkedList.class;
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testArrayList() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = ArrayList.class;
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = Map.class;
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testHashMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = HashMap.class;
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testSortedMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = SortedMap.class;
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testTreeMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = TreeMap.class;
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testLinkedHashMap() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = LinkedHashMap.class;
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.returnValueFor(type));
    }

    @Test
    public void testDefault() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Class<?> type = Object.class;
        assertNull(returnsEmptyValues.returnValueFor(type));
    }
}