
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        String result = (String) returnsEmptyValues.answer(invocation);
        // Test that the returned value contains the correct mock information
        assert result.contains("Mock for ReturnsEmptyValues");
        assert result.contains("hashCode: " + returnsEmptyValues.hashCode());
    }
    
    @Test
    public void testCompareTo() throws NoSuchMethodException {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        
        // Test that the returned value is 0 when references are the same
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getArguments()).thenReturn(new Object[] { invocation.getMock() });
        int result = (int) returnsEmptyValues.answer(invocation);
        assert result == 0;
        
        // Test that the returned value is 1 when references are different
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getArguments()).thenReturn(new Object[] { new Object() });
        result = (int) returnsEmptyValues.answer(invocation);
        assert result == 1;
    }
    
    @Test
    public void testReturnValueFor_Primitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test primitive types
        assert returnsEmptyValues.returnValueFor(int.class) == 0;
        assert returnsEmptyValues.returnValueFor(boolean.class) == false;
        assert returnsEmptyValues.returnValueFor(double.class) == 0.0;
    }
    
    @Test
    public void testReturnValueFor_PrimitiveWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test primitive wrapper types
        assert returnsEmptyValues.returnValueFor(Integer.class).equals(0);
        assert returnsEmptyValues.returnValueFor(Boolean.class).equals(false);
        assert returnsEmptyValues.returnValueFor(Double.class).equals(0.0);
    }
    
    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test collection types
        assert returnsEmptyValues.returnValueFor(Collection.class).getClass().equals(LinkedList.class);
        assert returnsEmptyValues.returnValueFor(Set.class).getClass().equals(HashSet.class);
        assert returnsEmptyValues.returnValueFor(HashSet.class).getClass().equals(HashSet.class);
        assert returnsEmptyValues.returnValueFor(SortedSet.class).getClass().equals(TreeSet.class);
        assert returnsEmptyValues.returnValueFor(TreeSet.class).getClass().equals(TreeSet.class);
        assert returnsEmptyValues.returnValueFor(LinkedHashSet.class).getClass().equals(LinkedHashSet.class);
    }
    
    @Test
    public void testReturnValueFor_List() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test list types
        assert returnsEmptyValues.returnValueFor(List.class).getClass().equals(LinkedList.class);
        assert returnsEmptyValues.returnValueFor(LinkedList.class).getClass().equals(LinkedList.class);
        assert returnsEmptyValues.returnValueFor(ArrayList.class).getClass().equals(ArrayList.class);
    }
    
    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test map types
        assert returnsEmptyValues.returnValueFor(Map.class).getClass().equals(HashMap.class);
        assert returnsEmptyValues.returnValueFor(HashMap.class).getClass().equals(HashMap.class);
        assert returnsEmptyValues.returnValueFor(SortedMap.class).getClass().equals(TreeMap.class);
        assert returnsEmptyValues.returnValueFor(TreeMap.class).getClass().equals(TreeMap.class);
        assert returnsEmptyValues.returnValueFor(LinkedHashMap.class).getClass().equals(LinkedHashMap.class);
    }
    
    @Test
    public void testReturnValueFor_OtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test other types
        assert returnsEmptyValues.returnValueFor(String.class) == null;
        assert returnsEmptyValues.returnValueFor(Object.class) == null;
        assert returnsEmptyValues.returnValueFor(ReturnsEmptyValues.class) == null;
    }
}
