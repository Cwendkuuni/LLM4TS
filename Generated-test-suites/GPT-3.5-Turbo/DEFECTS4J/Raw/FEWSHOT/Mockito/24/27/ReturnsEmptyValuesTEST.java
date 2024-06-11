import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(0, emptyValues.returnValueFor(int.class));
        assertEquals(0L, emptyValues.returnValueFor(long.class));
        assertEquals(0.0f, emptyValues.returnValueFor(float.class), 0.0);
        assertEquals(0.0, emptyValues.returnValueFor(double.class), 0.0);
        assertEquals('\u0000', emptyValues.returnValueFor(char.class));
        assertEquals(false, emptyValues.returnValueFor(boolean.class));
        assertEquals(0, emptyValues.returnValueFor(Integer.class));
        assertEquals(null, emptyValues.returnValueFor(String.class));
    }

    @Test
    public void testToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), emptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().spiedInstance(mock))));
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(1, emptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().name("compareTo"))));
    }
    
    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), emptyValues.returnValueFor(Collection.class));
    }
    
    @Test
    public void testReturnValueForSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new HashSet<Object>(), emptyValues.returnValueFor(Set.class));
    }
    
    @Test
    public void testReturnValueForHashSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new HashSet<Object>(), emptyValues.returnValueFor(HashSet.class));
    }
    
    @Test
    public void testReturnValueForSortedSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new TreeSet<Object>(), emptyValues.returnValueFor(SortedSet.class));
    }
    
    @Test
    public void testReturnValueForTreeSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new TreeSet<Object>(), emptyValues.returnValueFor(TreeSet.class));
    }
    
    @Test
    public void testReturnValueForLinkedHashSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedHashSet<Object>(), emptyValues.returnValueFor(LinkedHashSet.class));
    }
    
    @Test
    public void testReturnValueForList() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), emptyValues.returnValueFor(List.class));
    }
    
    @Test
    public void testReturnValueForLinkedList() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), emptyValues.returnValueFor(LinkedList.class));
    }
    
    @Test
    public void testReturnValueForArrayList() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new ArrayList<Object>(), emptyValues.returnValueFor(ArrayList.class));
    }
    
    @Test
    public void testReturnValueForMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new HashMap<Object, Object>(), emptyValues.returnValueFor(Map.class));
    }
    
    @Test
    public void testReturnValueForHashMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new HashMap<Object, Object>(), emptyValues.returnValueFor(HashMap.class));
    }
    
    @Test
    public void testReturnValueForSortedMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new TreeMap<Object, Object>(), emptyValues.returnValueFor(SortedMap.class));
    }
    
    @Test
    public void testReturnValueForTreeMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new TreeMap<Object, Object>(), emptyValues.returnValueFor(TreeMap.class));
    }
    
    @Test
    public void testReturnValueForLinkedHashMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedHashMap<Object, Object>(), emptyValues.returnValueFor(LinkedHashMap.class));
    }
}