import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
        returnsEmptyValues.methodsGuru = new ObjectMethodsGuru();
        returnsEmptyValues.mockUtil = new MockUtil();
    }
    
    @Test
    public void testAnswer_ToString() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(returnsEmptyValues.methodsGuru.isToString(Mockito.any())).thenReturn(true);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
        String expected = "Mock for " + returnsEmptyValues.mockUtil.getMockSettings(invocation.getMock()).getTypeToMock().getSimpleName()
            + ", hashCode: " + invocation.getMock().hashCode();
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswer_CompareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(returnsEmptyValues.methodsGuru.isCompareToMethod(Mockito.any()))
            .thenReturn(true);
        Mockito.when(invocation.getMock())
            .thenReturn(new Object())
            .thenReturn(invocation.getArguments()[0]);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[1]);
        
        Object result1 = returnsEmptyValues.answer(invocation);
        Object result2 = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result1);
        assertTrue(result1 instanceof Integer);
        assertNotNull(result2);
        assertTrue(result2 instanceof Integer);
        assertNotEquals(result1, result2);
    }
    
    @Test
    public void testReturnValueFor_PrimitiveOrWrapper() {
        Class<?> type1 = int.class;
        Class<?> type2 = Integer.class;
        
        Object result1 = returnsEmptyValues.returnValueFor(type1);
        Object result2 = returnsEmptyValues.returnValueFor(type2);
        
        assertNotNull(result1);
        assertEquals(0, (int) result1);
        assertNotNull(result2);
        assertEquals(0, (int) result2);
    }
    
    @Test
    public void testReturnValueFor_Collection() {
        Class<?> type = Collection.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_Set() {
        Class<?> type = Set.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_HashSet() {
        Class<?> type = HashSet.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_SortedSet() {
        Class<?> type = SortedSet.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_TreeSet() {
        Class<?> type = TreeSet.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Class<?> type = LinkedHashSet.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_List() {
        Class<?> type = List.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_LinkedList() {
        Class<?> type = LinkedList.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_ArrayList() {
        Class<?> type = ArrayList.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_Map() {
        Class<?> type = Map.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_HashMap() {
        Class<?> type = HashMap.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_SortedMap() {
        Class<?> type = SortedMap.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_TreeMap() {
        Class<?> type = TreeMap.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Class<?> type = LinkedHashMap.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }
    
    @Test
    public void testReturnValueFor_OtherType() {
        Class<?> type = StringBuilder.class;
        
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNull(result);
    }
}