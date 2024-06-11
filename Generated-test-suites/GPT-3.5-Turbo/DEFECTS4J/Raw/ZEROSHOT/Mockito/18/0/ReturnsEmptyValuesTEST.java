import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        String actual = (String) returnsEmptyValues.answer(mockitoInvocationOfToString(mock));
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareToSameReference() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);
        when(mockitoInvocationOfCompareTo(mock, mock)).thenReturn(0);
        int actual = (int) returnsEmptyValues.answer(mockitoInvocationOfCompareTo(mock, mock));
        assertEquals(0, actual);
    }

    @Test
    public void testCompareToDifferentReference() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(mockitoInvocationOfCompareTo(mock1, mock2)).thenReturn(1);
        int actual = (int) returnsEmptyValues.answer(mockitoInvocationOfCompareTo(mock1, mock2));
        assertEquals(1, actual);
    }

    @Test
    public void testReturnValueForPrimitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int expectedInt = 0;
        Integer expectedInteger = 0;
        assertEquals(expectedInt, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(expectedInteger, returnsEmptyValues.returnValueFor(Integer.class));
    }

    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedList<Object> expectedLinkedList = new LinkedList<>();
        HashSet<Object> expectedHashSet = new HashSet<>();
        assertEquals(expectedLinkedList, returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(expectedHashSet, returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(expectedHashSet, returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(expectedHashSet, returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(expectedHashSet, returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(expectedHashSet, returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(expectedLinkedList, returnsEmptyValues.returnValueFor(List.class));
        assertEquals(expectedLinkedList, returnsEmptyValues.returnValueFor(LinkedList.class));
        assertEquals(expectedLinkedList, returnsEmptyValues.returnValueFor(ArrayList.class));
    }

    @Test
    public void testReturnValueForMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashMap<Object, Object> expectedHashMap = new HashMap<>();
        TreeMap<Object, Object> expectedTreeMap = new TreeMap<>();
        assertEquals(expectedHashMap, returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(expectedHashMap, returnsEmptyValues.returnValueFor(HashMap.class));
        assertEquals(expectedTreeMap, returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(expectedTreeMap, returnsEmptyValues.returnValueFor(TreeMap.class));
        assertEquals(expectedTreeMap, returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }


    private InvocationOnMock mockitoInvocationOfToString(Object mock) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethods()[0]); // assume first method is toString()
        when(invocation.getMock()).thenReturn(mock);
        return invocation;
    }

    private InvocationOnMock mockitoInvocationOfCompareTo(Object mock1, Object mock2) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]); // assume first method is compareTo()
        when(invocation.getMock()).thenReturn(mock1);
        when(invocation.getArguments()).thenReturn(new Object[]{mock2});
        return invocation;
    }
}